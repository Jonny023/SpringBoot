<!DOCTYPE html>
<html lang="en">
<head>
<#--    <meta charset="UTF-8"/>-->
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" charset="UTF-8"/>
    <title>标签导出</title>
    <style>
        @page {
            size: A4;
            margin: 10mm;
            @top-center {
                content: element(header);
            }
            @bottom-center {
                font-family: 'simsun', serif;
            }
        }
        html, body {
            font-family: 'simsun', serif;
            font-size: 12px;
        }
        .column {
            width: 45%;
            float: left;
            padding-bottom: 43%;
        }
        table {
            border-collapse: collapse;
            width: 100%;
        }
        table, td, th {
            border: 1px solid #555;
            padding: 10px;
        }
        .column:nth-child(even) {
            margin-left: 50px;
        }
        .titleBox {
            width: 48px;
            font-weight: bold;
        }
        .contentBox {
            font-style: italic;
        }
    </style>
</head>
<body>
<div class="main">
    <#list products as item>
    <div class="column">
        <table>
            <tr>
                <td class="titleBox">产品名称</td>
                <td>${item.productName!}</td>
                <td rowspan="4" colspan="2" style="text-align: center"><img src="${item.qrcode!}" style="width:80px;height: 80px"/></td>
            </tr>
            <tr>
                <td class="titleBox">产品类型</td>
                <td class="contentBox">${item.productType!}</td>
            </tr>
            <tr>
                <td class="titleBox">产品编号</td>
                <td class="contentBox">${item.productNo!}</td>
            </tr>
            <tr>
                <td class="titleBox">硬件版本</td>
                <td class="contentBox">${item.hardwareVersion!}</td>
            </tr>
            <tr>
                <td class="titleBox">软件版本</td>
                <td class="contentBox">${item.softwareVersion!}</td>
                <td class="titleBox">联系电话</td>
                <td class="contentBox">${item.phone!}</td>
            </tr>
            <tr>
                <td class="titleBox">生产日期</td>
                <td class="contentBox" style="width: 60px">${item.dateOfManufacture!}</td>
                <td class="titleBox">生产厂家</td>
                <td class="contentBox">${item.manufacturer!}</td>
            </tr>
        </table>
    </div>
    </#list>
</div>
</body>
</html>
