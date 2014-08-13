curl -XPOST localhost:12060/repository/schema/recordType -H 'Content-Type: application/json' -d '
{
action : "create",
recordType: {
name: "n$file",
fields: [
{ name: "n$data", mandatory: true}
],
namespaces: { "my.demo": "n" }
}
}' -D -
