(function() {
  'use strict';
  var elements = {};
  var currentUsername;

  function usernameEditStart() {
    currentUsername = elements.usernameLabel.textContent;
    elements.usernameInput.value = currentUsername;
    elements.usernameEdit.style.display = 'block';
    elements.usernameLabel.style.display = 'none';
    elements.usernameInput.focus();
  }

  function usernameEditConfirm() {
    var newName = elements.usernameInput.value.trim();
    if(newName === currentUsername || newName === '') {
      usernameEditCancel();
    } else {
      console.log('Saving new username [' + newName + '].');
      var url = '/options/set/username/' + encodeURIComponent(newName);
      var request = new XMLHttpRequest();
      request.onreadystatechange = function() {
        if (request.readyState === 4) {
          switch(request.responseText) {
            case 'OK':
              elements.profileLink.textContent = newName;
              elements.usernameLabel.textContent = newName;
              elements.usernameEdit.style.display = 'none';
              elements.usernameLabel.style.display = 'inline-block';
              break;
            case '':
              break;
            default:
              if(request.responseText.indexOf('ERROR:') === 0) {
                elements.usernameEditError.textContent = request.responseText.substr(6);
              } else {
                throw request.responseText;
              }
              break;
          }
        }
      };
      request.open('GET', url, true);
      request.send();
    }
  }

  function usernameEditCancel() {
    elements.usernameInput.value = currentUsername;
    elements.usernameEdit.style.display = 'none';
    elements.usernameLabel.style.display = 'inline-block';
  }

  function saveUsername() {
    var url = '/options/set/username/' + elements.usernameInput.value;
    var request = new XMLHttpRequest();
    request.open('GET', url, true);
    request.send();
  }

  function init() {
    elements.usernameContainer = document.getElementById('username-container');
    if(elements.usernameContainer !== null) {
      elements.profileLink = document.getElementById('profile-link');
      elements.usernameLabel = document.getElementById('username-label');
      elements.usernameEdit = document.getElementById('username-edit');
      elements.usernameInput = document.getElementById('username-input');
      elements.usernameEditError = document.getElementById('username-edit-error');
      elements.usernameEditConfirm = document.getElementById('username-edit-confirm');
      elements.usernameEditCancel = document.getElementById('username-edit-cancel');

      elements.usernameInput.onkeypress = function(e) {
        if (e.keyCode === 13) {
          usernameEditConfirm();
          e.preventDefault();
        } else if(e.keyCode === 27) {
          usernameEditCancel();
          e.preventDefault();
        }
      };
      elements.usernameLabel.onclick = usernameEditStart;
      elements.usernameEditConfirm.onclick = saveUsername;
      elements.usernameEditConfirm.onclick = usernameEditConfirm;
      elements.usernameEditCancel.onclick = usernameEditCancel;
    }
  }

  document.addEventListener('DOMContentLoaded', init);
})();
