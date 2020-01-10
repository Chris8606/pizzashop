<html>
<head>
    <meta charset="utf-8">
    <title>Pizzas</title>
    <link rel="stylesheet" href="css/pizzas.css">


</head>
<body>
  <div class="header">
      <div class="container1">
        <div class="links">
          <a href="home">Home</a>
          <a>|</a>
          <a href="pizza">Angebot</a>
          <a>|</a>
          <a href="order">Bestellung</a>
        </div>
      </div>
    </div>
    <div class="content">
      <div class="container2">
        <div class="tabelle">
          <table>
            <tr>
                <th> Name </th>
                <th> Price </th>
            </tr>
            <#list pizzas as pizza>
                <tr>
                    <td> ${pizza.name} </td>
                    <td> ${pizza.price} </td>
                </tr>
            </#list>
          </table>
        </div>
      </div>
    </div>
    <div class="footer">
    </div>
</body>
</html>