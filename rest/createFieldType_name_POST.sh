curl -XPOST localhost:12060/repository/schema/fieldType -H 'Content-Type: application/json' -d '
{
action: "create",
fieldType: {
name: "n$name",
valueType: "STRING",
scope: "versioned",
namespaces: { "my.demo": "n" }
}
}' -D -
	