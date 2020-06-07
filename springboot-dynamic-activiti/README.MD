# springboot2.3.0动态创建流程

## 注意
* `mysql8.0`不能建表，需要将驱动修改到8.0以下

## 业务表单和Activiti关联

* 将业务数据id存入`act_ru_execution`表中对应`BUSINESS_KEY`字段