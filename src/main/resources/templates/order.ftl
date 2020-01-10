<html>
<head>
    <meta charset="utf-8">
    <title>Bestellung</title>
    <link rel="stylesheet" href="css/order.css">
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
        Bitte hier die Bestellung angeben, Ihre Daten werden nicht an Dritte weitergegeben :)

        <div class="bestellung">
            <br>
            <form method="post" action="order">
                <#list pizzas as pizza>
                    <select id="amount-${pizza.id}" name="amount-${pizza.id}">
                        <option value="0">0</option>
                        <option value="1">1</option>
                        <option value="2">2</option>
                        <option value="3">3</option>
                        <option value="4">4</option>
                        <option value="5">5</option>
                    </select>
                    ${pizza.name} ${pizza.price} CHF
                    <br>
                </#list>

        <p>
            <label>Adresse:<input name="address" type="text"></label>
            <br>
            <label>Telefonnummer:<input name="phonenumber" type="text"></label>
            <br>
            <label><input id="submit" type="submit" value="Abschicken"></label>
        </p>
        </form>
    </div>
</div>
</div>
<div class="footer">
</div>
</body>
</html>