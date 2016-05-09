<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>${title}</title>

</head>
<body>
    <h1> Welcome to ${title}! </h1>
    <p>We have these transactions:
    <table border=1>
      <#list transactions as transaction>
        <tr><td>${transaction.id}</td><td>${transaction.timestamp}</td><td>${transaction.description}</td></tr>
      <#else>
        No transactions found.
      </#list>
    </table>
</body>
</html>