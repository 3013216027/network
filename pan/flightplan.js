// flightplan.js
var plan = require('flightplan');

// configuration
plan.target('staging', {
  host: 'zhengdongjian.xyz',
  username: 'dong',
  agent: process.env.SSH_AUTH_SOCK
});

plan.target('production', [
  {
    host: 'zhengdongjian.xyz',
    username: 'dong',
    agent: process.env.SSH_AUTH_SOCK
  }
]);

var tmpDir = 'example-com-' + new Date().getTime();

// run commands on localhost
plan.local(function(local) {
  local.log('Run build');
  local.exec('gulp build');

  local.log('Copy files to remote hosts');
  var filesToCopy = local.exec('git ls-files', {silent: true});
  // rsync files to all the target's remote hosts
  local.transfer(filesToCopy, '/tmp/' + tmpDir);
});

// run commands on the target's remote hosts
plan.remote(function(remote) {
  remote.log('Move folder to web root');
  remote.exec('cp -R /tmp/' + tmpDir + ' ~/git/network/', {user: 'dong'});
  remote.rm('-rf /tmp/' + tmpDir);

  remote.log('Install dependencies');
  remote.exec('npm --production --prefix ~/' + tmpDir
                            + ' install ~/' + tmpDir, {user: 'dong'});

  remote.log('Reload application');
  //remote.exec('ln -snf ~/' + tmpDir + ' ~/example-com', {user: 'dong'});
  //remote.sudo('pm2 reload example-com', {user: 'dong'});
});

// run more commands on localhost afterwards
plan.local(function(local) { /* ... */ });
// ...or on remote hosts
plan.remote(function(remote) { /* ... */ });
