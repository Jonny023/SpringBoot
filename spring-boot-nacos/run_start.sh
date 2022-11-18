current=`date "+%Y-%m-%d %H:%M:%S"`
timeStamp=`date -d "$current" +%s`
#将current转换为时间戳，精确到毫秒
currentTimeStamp=$((timeStamp*1000+`date "+%N"`/1000000))
echo $currentTimeStamp
#项目名称
projectName=springboot-nacos
#版本号
version=v1.0
name=${projectName}_${version}_${currentTimeStamp}
port=8088

docker rm -f $(docker ps -a | grep ${projectName}_${version} | awk '{print $1}')
docker build -t $name .
docker run -itd --name test --network=host --restart=always -p ${port}:${port} -v /usr/local/project/boot_nacos:/usr/local/project/boot_nacos $name