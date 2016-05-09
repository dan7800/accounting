<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>${title}</title>

</head>
<body>
    <h1> Welcome to ${title}! </h1>
    <p>We have these transactions:
    <#list transactions as transaction>
      #${transaction.id} : ${transaction.timestampString} - ${transaction.description}
      <table border=1>
      <tr>
        <th>ID</th>
        <th>TO</th>
        <th>FROM</th>
        <th>AMOUNT</th>
      </tr>
      <#list transaction.entries as entry>
        <tr>
            <td>${entry.id}</td>
            <td>${entry.toAccount}</td>
            <td>${entry.fromAccount}</td>
            <td>${entry.amount}</td>
        </tr>
      <#else>
        <tr><td colspan="4">No entries found.</td></tr>
      </#list>
      </table>
    <#else>
        No transactions found.
    </#list>
</body>
</html>