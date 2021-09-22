# 动态创建流程图（通过代码创建流程图）
### pom依赖

```xml
<!-- activiti -->
<dependency>
	<groupId>org.activiti</groupId>
	<artifactId>activiti-spring-boot-starter-basic</artifactId>
	<version>${activiti.version}</version>
	<exclusions>
		<exclusion>
			<groupId>de.odysseus.juel</groupId>
			<artifactId>juel-spi</artifactId>
		</exclusion>
		<exclusion>
			<groupId>de.odysseus.juel</groupId>
			<artifactId>juel-api</artifactId>
		</exclusion>
		<exclusion>
			<artifactId>activation</artifactId>
			<groupId>javax.activation</groupId>
		</exclusion>
		<exclusion>
			<artifactId>mybatis</artifactId>
			<groupId>org.mybatis</groupId>
		</exclusion>
	</exclusions>
</dependency>
<dependency>
	<groupId>org.activiti</groupId>
	<artifactId>activiti-json-converter</artifactId>
	<version>${activiti.version}</version>
</dependency>

<!-- activiti整合动态流程图所需引入的pom文件-->
<dependency>
	<groupId>org.activiti</groupId>
	<artifactId>activiti-bpmn-layout</artifactId>
	<version>${activiti.version}</version>
</dependency>
```

## 红色版流程跟踪

```java
@Autowired
    private HistoryService historyService;
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private ProcessEngineConfiguration processEngine;
    @Autowired
    private RuntimeService runtimeService;
    // TODO

    /**
     * 根据流程实例id
     * @param processInstanceId 流程实例id
     */
    @RequestMapping("/img")
    @ResponseBody
    public ResponseApi generateProcessImageBase64(String processInstanceId) throws IOException {
        //获取历史流程实例
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        //获取历史流程定义
        ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity) ((RepositoryServiceImpl)
                repositoryService).getDeployedProcessDefinition(historicProcessInstance.getProcessDefinitionId());
        //查询历史节点，需要按照执行顺序排序，因为程序中需要判断流程最后的节点是否为结束节点
        //按照开始时间进行升序
        List<HistoricActivityInstance> historicActivityInstanceList = historyService.createHistoricActivityInstanceQuery().processInstanceId(processInstanceId)
                .orderByHistoricActivityInstanceStartTime().asc().list();
        //已执行历史节点
        List<String> executedActivityIdList = new ArrayList();
        for (HistoricActivityInstance historicActivityInstance : historicActivityInstanceList) {
            executedActivityIdList.add(historicActivityInstance.getActivityId());
        }
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionEntity.getId());
        //已执行flow的集和
        List<String> executedFlowIdList = executedFlowIdList(bpmnModel, historicActivityInstanceList);
        ProcessDiagramGenerator processDiagramGenerator = processEngine.getProcessEngineConfiguration()
                .getProcessDiagramGenerator();
        InputStream inputStream = processDiagramGenerator.generateDiagram(bpmnModel, "png", executedActivityIdList,
                executedFlowIdList, processEngine.getProcessEngineConfiguration().getActivityFontName(),
                processEngine.getProcessEngineConfiguration().getLabelFontName(), processEngine
                        .getProcessEngineConfiguration().getAnnotationFontName(), processEngine
                        .getProcessEngineConfiguration().getClassLoader(), 1.0);
        OutputStream outputStream = new FileOutputStream("d:\\img1.png");
        int total = 0;
        byte[] bytes = new byte[1024];
        while ((total = inputStream.read(bytes)) != -1) {
            outputStream.write(bytes, 0, total);
        }
        inputStream.close();
        outputStream.close();
        return ResponseApi.success("成功");
    }

    /**
     * 已执行flow集合
     * @param bpmnModel                    模型
     * @param historicActivityInstanceList 已执行的节点，需要按照执行顺序排序，因为程序中需要判断流程最后的节点是否为结束节点
     * @return 已执行的flow
     */
    private static List<String> executedFlowIdList(BpmnModel bpmnModel, List<HistoricActivityInstance> historicActivityInstanceList) {
        List<String> executedFlowIdList = new ArrayList();
        int count = historicActivityInstanceList.size();
        if (!"endEvent".equals(historicActivityInstanceList.get(count - 1).getActivityType())) {
            count = count - 1;
        }
        for (int i = 0; i < count; i++) {
            HistoricActivityInstance hai = historicActivityInstanceList.get(i);
            FlowNode flowNode = (FlowNode) bpmnModel.getFlowElement(hai.getActivityId());
            List<SequenceFlow> sequenceFlows = flowNode.getOutgoingFlows();
            if (sequenceFlows.size() > 1) {
                HistoricActivityInstance nextHai = historicActivityInstanceList.get(i + 1);
                for (SequenceFlow sequenceFlow : sequenceFlows) {
                    if (sequenceFlow.getTargetRef().equals(nextHai.getActivityId())) {
                        executedFlowIdList.add(sequenceFlow.getId());
                    }
                }
            } else {
                if (sequenceFlows.size() != 0) executedFlowIdList.add(sequenceFlows.get(0).getId());
            }
        }
        return executedFlowIdList;
    }
```