#!/bin/sh
sudo docker run --name boilerplay_db -d -p 5432:5432 -e POSTGRES_USER=boilerplay -e POSTGRES_DB=boilerplay -e POSTGRES_password=bolierplay postgres:9.6.1-alpine

