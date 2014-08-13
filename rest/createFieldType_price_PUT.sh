curl -XPUT localhost:12060/repository/schema/fieldType/n\$price?ns.n=my.demo -H 'Content-Type: application/json' -d '
{
name: "n$price",
valueType: "DECIMAL",
scope: "versioned",
namespaces: { "my.demo": "n" }
}' -D -
