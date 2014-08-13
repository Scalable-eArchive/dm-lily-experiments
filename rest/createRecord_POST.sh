curl -XPOST localhost:12060/repository/record -H 'Content-Type: application/json' -d '
{
action : "create",
record: {
type: "n$product",
fields: {
n$name: "Bread",
n$price: 2.11
},
namespaces: { "my.demo": "n" }
}
}' -D -
