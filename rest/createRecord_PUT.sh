curl -XPUT localhost:12060/repository/record/UUID.04df659c-bf48-4274-bab4-117cd11a09fa -H 'Content-Type: application/json' -d '
{
type: "n$product",
fields: {
n$name: "Butter",
n$price: 4.25
},
namespaces: { "my.demo": "n" }
}' -D -
