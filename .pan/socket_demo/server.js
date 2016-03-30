const net = require('net');

var clientList = [];

net.createServer((client) => {
  console.log('connect to server!');
  client.name = client.remoteAddress + ':' + client.remotePort;
  this.name = client.localAddress + ':' + client.localPort;
  client.write('I\'m server of ' + this.name);
  console.log('Hello from ' + client.name + '!\r\n');
  clientList.push(client);
  client.on('data', (data) => {
    /* broadcast */
    for (var i = 0; i < clientList.length; ++i) {
      if (clientList[i].writable) {
        clientList[i].write(client.name + ' says ' + data);
      }
    }
    console.log(client.name + ' says ' + data);
  });
  client.on('end', (err) => {
    console.log('disconnect server!');
    clientList.splice(clientList.indexOf(client), 1);
  });
  client.on('error', (err) => {
    throw err;
  });
}).listen(3000);
