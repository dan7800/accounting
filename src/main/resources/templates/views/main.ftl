<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Accounting</title>

</head>
<body>
    <h1> Welcome ! </h1>
    <p>We have these transactions:
    <table border=1>
      <#list transactions as transaction>
        <tr><td>${transaction.id}<td><td>${transaction.description}</td></tr>
      </#list>
    </table>
</body>
</html>