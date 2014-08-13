curl -XPUT localhost:12060/repository/record/USER.my_article -H 'Content-Type: application/json' -d '
{
type: "a$article",
fields: {
a$title: "Title of the article",
a$authors: [
{
type: "a$author",
fields: {
a$name: "Author X",
a$email: "author_x@authors.com"
}
},
{
type: "a$author",
fields: {
a$name: "Author X",
a$email: "author_x@authors.com"
}
}
],
a$body: "Body text of the article"
},
namespaces: { "article": "a" }
}' -D -
