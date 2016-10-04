package services.translation

import java.io.{File, PrintWriter}

import play.api.libs.concurrent.Execution.Implicits.defaultContext
import services.translation.api.{BingApi, YandexApi}

import scala.concurrent.Future
import scala.io.Source

import utils.Language

@javax.inject.Singleton()
class TranslationService @javax.inject.Inject() (yandex: YandexApi, bing: BingApi) {
  def parse(f: File) = if (f.isFile) {
    Source.fromFile(f).getLines.flatMap { line =>
      val split = line.trim.replaceAllLiterally("''", "'").split('=')
      if (split.length < 2) {
        None
      } else {
        Some(split.head.trim -> split.tail.mkString("=").trim)
      }
    }.toList
  } else {
    Nil
  }

  def translate(apiName: String, main: Seq[(String, String)], lang: String, current: Map[String, String], force: Boolean) = {
    val api = getApi(apiName)
    val futures = main.map { en =>
      current.get(en._1) match {
        case Some(value) if !force => Future.successful(Some(en._1 -> value))
        case _ => api.translate(lang, en._1, en._2)
      }
    }
    val contents = Future.sequence(futures).map(_.flatten)
    contents
  }

  def translateAll(api: String, root: java.io.File, force: Boolean = false) = {
    val mainFile = new java.io.File(root, "messages")
    val mainTranslations = parse(mainFile)

    val files = Language.values.filterNot(_.code == "en").map { lang =>
      val f = new java.io.File(root, "messages." + lang.code)
      val current = parse(f).toMap
      val ts = translate(api, mainTranslations, lang.code, current, force)
      val contents = ts.map(seq => seq.map(x => s"${x._1} = ${x._2.replaceAllLiterally("'", "''")}").mkString("\n"))
      contents.flatMap { c =>
        new PrintWriter(f) {
          write(c)
          close()
        }
        ts.map(x => lang.code -> x)
      }
    }
    mainTranslations -> Future.sequence(files)
  }

  private[this] def getApi(name: String) = name match {
    case "yandex" => yandex
    case "bing" => bing
    case x => throw new IllegalArgumentException(s"Invalid api [$x].")
  }
}
