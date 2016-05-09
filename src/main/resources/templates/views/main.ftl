<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>${title}</title>
    <link rel="stylesheet" type="text/css" href="http://yui.yahooapis.com/pure/0.6.0/pure-min.css">
</head>
<body>
    <h1> Welcome to ${title}! </h1>
    <p>We have these transactions:
    <#list transactions as transaction>
      <div>
        <h3> #${transaction.id} : ${transaction.timestampString} - ${transaction.description} </h3>
        <table border=1 class="pure-table">
        <thead>
            <tr>
                <th>ID</th>
                <th>TO</th>
                <th>FROM</th>
                <th>AMOUNT</th>
            </tr>
        </thead>
        <tbody>
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
        </tbody>
        </table>
      </div>
    <#else>
        No transactions found.
    </#list>
</body>
</html>