## 工作流

### 流程设计器

> idea高版本actiBPM插件已经不可用了

* [Camunda Modeler](Camunda Modeler)
* [Camunda设计器入门](https://www.bilibili.com/video/BV1Ng411S7Ve?p=5&vd_source=610e097b4d28ca7a9353304c7307c4a9)
* [Camunda兼容activiti](https://it.cha138.com/php/show-39038.html)

> eclipse安装`Activiti BPMN 2.0 designer`

* Help > Install new Software > Add

```shell
Name: Activiti BPMN 2.0 designer
Location: http://activiti.org/designer/update/
```

* 注意：**用对象作为流程实例变量必须实现序列化接口Serializable**

## 流程变量

### 启动流程实例时指定

```java
@Test
public void testStartProcessByVariable2() {
    ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
    RuntimeService runtimeService = engine.getRuntimeService();
    String key = "my_leave";
    Map<String, Object> variables = new HashMap<String, Object>();
    Evection evection = new Evection();
    evection.setNum(3D);
    variables.put("evection", evection);
    variables.put("assignee0", "张三1");
    variables.put("assignee1", "李四1");
    variables.put("assignee2", "总经理1");
    variables.put("assignee3", "财务1");

    runtimeService.startProcessInstanceByKey(key, variables);
}
```

### 完成任务时指定下一个审批人

```java
/**
 * 完成任务并指定审批人
 */
@Test
public void completeTask1() {
    ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
    String key = "my_leave";
    String assignee = "总经理1";
    TaskService taskService = engine.getTaskService();
    Task task = taskService.createTaskQuery().processDefinitionKey(key)
            .taskAssignee(assignee)
            .singleResult();
    if (task != null) {
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("assignee3", "admin");
        taskService.complete(task.getId(), variables);
    }
}
```