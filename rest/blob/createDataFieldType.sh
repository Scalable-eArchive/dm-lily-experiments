curl -XPOST localhost:12060/repository/schema/fieldType -H 'Content-Type: application/json' -d '
{
action : "create",
fieldType: {
name: "n$data",
valueType: "BLOB",
scope: "versioned",
namespaces: { "my.demo": "n" }
}
}' -D -
