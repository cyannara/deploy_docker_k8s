const express = require('express')
const app = express()
const port = 3000

app.get('/', (req, res) => {
  res.send('Hello 홍길동!')
})

app.listen(port, () => {
  console.log(`Example app listening on port ${port}`)
})