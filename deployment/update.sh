cd wk_server
git pull origin
mvn -DskipTests=true install
cd ..
git pull origin

echo "test"
if [ -z "$1" ]; then
	mvn -DskipTests=true install
else
	mvn -DskipTests=true install -pl $1 -am
fi
