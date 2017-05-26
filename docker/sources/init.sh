#/bin/bash

pwd=`pwd`

"$pwd"/docker/sources/postgres/init.sh
"$pwd"/docker/sources/mongodb/init.sh

exit 0