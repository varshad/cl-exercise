let eventSource;
let started = false;

function start() { // when "Start" button pressed
  if (!window.EventSource) {
    // IE or an old browser
    alert("The browser doesn't support EventSource.");
    return;
  }

  if (!validateForm()) {
      return;
  }

  let duration = document.getElementById('duration').value;
  let url = 'http://localhost:10000/start?times=' + duration;
  eventSource = new EventSource(url);
  started = true;

  eventSource.onerror = function(event) {
    log("Event: error");
    if (this.readyState == EventSource.CONNECTING) {
      log(`Reconnecting (readyState=${this.readyState})...`);
    } else {
      log("Error has occured.");
    }
  };

  eventSource.onmessage = function(event) {
    log("Event: message, data: " + event.data);

    var data = JSON.parse(event.data);
    if (data.connectionStatus == 'NOT_CONNECTED') {
        eventSource.close();
    }
  };
}

async function stop() { // when "Stop" button pressed
  if (started) {
     const response = await fetch('http://localhost:10000/stop', {
        method: 'POST'
     });
     console.log(response);
  }
}

function log(msg) {
  logElement.innerHTML += msg + "<br>";
  //document.documentElement.scrollTop = 99999999;
}

function validateForm() {
  let duration = document.getElementById('duration').value;

  if (duration == "" || isNaN(duration) || duration < 0 || duration > 10) {
    alert("Invalid duration. Must be within 0-10 seconds");
    return false;
  } else {
    return true;
  }
}
