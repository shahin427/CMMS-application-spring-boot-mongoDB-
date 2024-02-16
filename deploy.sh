
# --action build , upload , run

################################################################################################# Variables
appName='cmms.jar'

root='';
base="/home";

################################################################################################# Convert arguments to variables
while [ $# -gt 0 ]; do
	if [[ $1 == *"--"* ]]; then
		v="${1/--/}"
		declare $v="$2"
	fi
	shift
done
################################################################################################ pro 183
if [ "$pro" == "183" ]
then
  echo "183"
  address="/home/shahin/cmms";
  runAddress="/home/shahin/cmms";
  user='shahin'
  att='@'
  ip='94.232.173.183'
fi

################################################################################################ Build
if [ "$action" == "build" ]
then
  echo "build -> upload -> run"
	# remove target directory
	rm -rf target

#	 Build
	mvn clean package
	if ! [ $? -eq 0 ];
	then
		echo "Build fail [mvn clean package]"
		exit
	else
	  scp -P 10022 target/cmms.jar  $user$att$ip:$address
	  if ! [ $? -eq 0 ]; then
			echo "Uploading new jar file error!"
			exit
		fi
  if [ "$pro" == "183" ]; then
    ssh -p 10022 $user$att$ip
    if ! [ $? -eq 0 ]; then
      echo "Running new jar file error!"
      exit
    fi
  fi
	fi
fi

################################################################################################ Upload
if [ "$action" == "upload" ]
then
  echo "upload -> run"
  scp -P 10022 target/cmms.jar  $user$att$ip:$address
  if ! [ $? -eq 0 ]; then
    echo "Uploading new jar file error!"
    exit
  fi
  # Run file
  if [ "$pro" == "183" ]; then
    ssh -p 10022 $user$att$ip
    if ! [ $? -eq 0 ]; then
      echo "Running new jar file error!"
      exit
    fi
  fi
fi
################################################################################################ Run
if [ "$action" == "run" ]
then
  echo "run"
  if [ "$pro" == "183" ]; then
    ssh -p 10022 $user$att$ip
    if ! [ $? -eq 0 ]; then
      echo "Running new jar file error!"
      exit
    fi
  fi
fi

################################################################################################ Init
if [ "$init" == "1" ]
then
  runFileName='run.sh'
  echo "init in"
  echo $pro' , run file name : '$runFileName
  echo 'address : ' : $runAddress
  scp -P 10022 $runFileName  $user$att$ip:$runAddress
  if ! [ $? -eq 0 ]; then
    echo "Uploading run file error!"
    exit
  fi
fi