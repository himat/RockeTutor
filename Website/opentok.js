var apiKey    = "44704232";
  var sessionId = "1_MX40NDcwNDIzMn5-U2F0IE1hciAyMiAxODoyNzowNSBQRFQgMjAxNH4wLjM1OTc0OTYyfg";
  var token     = "T1==cGFydG5lcl9pZD00NDcwNDIzMiZzZGtfdmVyc2lvbj10YnJ1YnktdGJyYi12MC45MS4yMDExLTAyLTE3JnNpZz1kM2MyMDlhMTNiYjQ0NGRhYjJkZDljNGMyYzY5NDNjNGE0MjM3ODViOnJvbGU9cHVibGlzaGVyJnNlc3Npb25faWQ9MV9NWDQwTkRjd05ESXpNbjUtVTJGMElFMWhjaUF5TWlBeE9Eb3lOem93TlNCUVJGUWdNakF4Tkg0d0xqTTFPVGMwT1RZeWZnJmNyZWF0ZV90aW1lPTEzOTU1MzgwNzYmbm9uY2U9MC4xNDAzNzcyODYwMDE5MDY0NiZleHBpcmVfdGltZT0xMzk1NjI0NDIxJmNvbm5lY3Rpb25fZGF0YT0=";
 
  //Random controllable shiznits
  var widthpub     = 300;
  var heightpub    = 200;
  var widthsub     = 400;
  var heightsub    = 300;
  var publishername =  "Abhishek";

////setTimeout(function,time);
    
 
  function sessionConnectedHandler (event) {
     session.publish( publisher );
     subscribeToStreams(event.streams);
  }
  function subscribeToStreams(streams) {
    for (var i = 0; i < streams.length; i++) {
        var stream = streams[i];
        if (stream.connection.connectionId 
               != session.connection.connectionId) {
            var subscriber = session.subscribe(stream,document.getElementById("bob"), {width:widthsub, height:heightsub});
        }
    }
  }
  function streamCreatedHandler(event) {
    subscribeToStreams(event.streams);
  }
 
  var publisher = TB.initPublisher(apiKey,"myPublisher",{width:widthpub, height:heightpub, name: publishername});
  var session   = TB.initSession(sessionId);
 
  session.connect(apiKey, token);
  session.addEventListener("sessionConnected", 
                           sessionConnectedHandler);
 
  session.addEventListener("streamCreated", 
                           streamCreatedHandler);
						   
						   
						   var connectionCount = 0;
TB.setLogLevel(TB.DEBUG);
TB.addEventListener("exception", exceptionHandler);
 
function connect() {
    if (TB.checkSystemRequirements() == 1) {
        var session = TB.initSession(sessionID);
        session.addEventListener("connectionCreated",
                                 connectionCreatedHandler);
        session.addEventListener("connectionDestroyed",
                                 connectionDestroyedHandler);
        session.connect(apiKey, token);
 
    } else {
        TB.log("The client does not support WebRTC.");
    }
 
    session.addEventListener("sessionConnected",
                              sessionConnectHandler);
    session.connect(apiKey, token);
}
 
function disconnect() {
    session.addEventListener("sessionDisconnected",
                             sessionDisconnectHandler);
    session.connect(apiKey, token);
}
 
function sessionDisconnectHandler(event) {
    // The event is defined by the SessionDisconnectEvent class
    if (event.reason == "networkDisconnected") {
        alert("Your network connection terminated.")
    }
}
 
 
 
function sessionConnectHandler(event) {
    connectionCount = event.connections.length;
    TB.log(connectionCount);
}
 
function connectionCreatedHandler(event) {
    connectionCount += event.connections.length;
    TB.log(connectionCount);
}
 
function connectionDestroyedHandler(event) {
    connectionCount -= event.connections.length;
    TB.log(connectionCount);
}
 
function exceptionHandler(event) {
    if (event.code == 1006) {
        // Connection failed
    }
}