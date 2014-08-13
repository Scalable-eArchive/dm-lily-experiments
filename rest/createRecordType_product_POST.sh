curl -XPOST localhost:12060/repository/schema/recordTypeById -H 'Content-Type: application/json' -d '
{
action: "create",
recordType: {
name: "n$product",
fields: [
{ name: "n$name", mandatory: true},
{ name: "n$price", mandatory: true}
],
namespaces: { "my.demo": "n" }
}
}' -D -
