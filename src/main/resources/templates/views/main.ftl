<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>${title}</title>
    <link rel="stylesheet" type="text/css" href="http://yui.yahooapis.com/pure/0.6.0/pure-min.css">
    <script type="text/javascript">
    function makeInvestment() {
        var request = new XMLHttpRequest();
        request.open('POST', 'investment?apiKey=investment', true);
        request.setRequestHeader('Content-Type', 'application/json; charset=UTF-8');
        request.send('{"amount":100000,"description":"investing"}');
        alert("Investment request sent.");
        location.reload();
    }
    </script>
</head>
<body>
    <h1> Welcome to ${title}! </h1>
    <h2>Accounts:</h2>
    <div>
      <table border=1 class="pure-table">
        <thead>
          <tr>
            <th>NAME</th>
            <th>BALANCE</th>
          </tr>
        </thead>
        <tbody>
          <#list accounts as account>
            <tr>
              <td>${account.name}</td>
              <td>${account.balance}</td>
            </tr>
          </#list>
        </tbody>
      </table>
    </div>
    <button type="button" onclick="makeInvestment()">Invest $100,000</button>
    <br/>
    <form action="/pdf-report" method="get">
        <input type="submit" value="Generate PDF Report">
    </form>
    <br/>
    <div>We have these transactions:
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
    </div>
</body>
</html>