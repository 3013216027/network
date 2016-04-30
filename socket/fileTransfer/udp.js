const dgram = require('dgram');
const server = dgram.createSocket('udp4');

server.on('error', (err) => {
  console.log(`server error:\n${err.stack}`);
  server.close();
});

server.on('message', (msg, info) => {
  console.log(`server got message: [${msg}] from ${info.address}:${info.port}`);
});

server.bind(1234, '', () => {
  var address = server.address();
  console.log(`server listening ${address.address}:${address.port}`);
});
