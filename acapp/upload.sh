#! /bin/bash

find dist/js/*.js | xargs sed -i 's/(function(){var e={/const myfunc = (function(myappid, AcWingOS){var e={/g'

find dist/js/*.js | xargs sed -i 's/.mount("#app")}()})();/.mount(myappid)}()});/g'

echo "

export class Game {
    constructor(id, AcWingOS) {
        const myappid = '#' + id;
        myfunc(myappid);
    }
}" >> dist/js/*.js

scp dist/js/*.js server1:kob/acapp
scp dist/css/*.css server1:kob/acapp

ssh server1 'cd kob/acapp && ./rename.sh'

