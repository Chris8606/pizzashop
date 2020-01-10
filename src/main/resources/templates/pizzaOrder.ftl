<html>
  <head>
    <meta charset="utf-8">
    <title>Bestellung</title>
    <link rel="stylesheet" href="css/pizzaOrder.css">
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
      <div class="bestelluebersicht">
      Bestell&uuml;bersicht
      </div>
        <div class = "bestellung">


          <table>
            <tr>
                <th> Anzahl </th>
                <th> Pizza </th>
                <th> Total(davon Mwst. 7.7 %:  ${order.calculatedVAT()}) </th>
            </tr>
            <div class = "liste">
            <#list pizzaOrderList as pizzaOrder>
                <tr>
                    <td> ${pizzaOrder.amount} </td>
                    <td> ${pizzaOrder.pizza.name} </td>
                    <td> ${pizzaOrder.price()}.00 </td>
                </tr>
            </#list>
            </div>
          </table>
        </div>
        <div class ="total_mehrBestellen">
            <p>
            Total:  <span class="total">${order.totalprice}.00 CHF</span>

            <div class= "mehrBestellen">
                <a href="order">Noch mehr bestellen !</a>
            </div>
            </p>
        </div>
        <div class = "dateAddressPhonenumber">
           ${order.address}, ${order.phonenumber}, ${order.localDate}
        </div>
      </div>
    </div>
    <div class="footer">
    </div>
  </body>
</html>