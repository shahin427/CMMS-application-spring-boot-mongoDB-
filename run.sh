cd /shahin/cmms

echo "satrt running ... date = " + date

port='8081'
app='cmms.jar'


################################################################################################# Convert arguments to variables
while [ $# -gt 0 ]; do
	if [[ $1 == *"--"* ]]; then
		v="${1/--/}"
		declare $v="$2"
	fi
	shift
done
################################################################################################ Run
if [ "$action" == "run" ]
then
  echo "run selected."
  PID=$(lsof -t -i:$port)
  if [ $PID ]
  then
    echo "port exists,killing pid..."
    kill -9 $PID ; sleep 5
    if ! [ $? -eq 0 ]; then
      echo "kill error!"
      exit
    fi
  fi
  java -jar $app
fi