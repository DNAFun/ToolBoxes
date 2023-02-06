<script>
export default {
  name: "NotFoundPage",
  data() {
    return {
      pic: require("../../../public/img/NotFound.jpeg")
    }
  },
  render(h) {
    return h('div', {
      id: "not-found-page"
    }, [
      h('img', {
        attrs: {
          src: this.pic
        }
      })
    ])
  }
}
</script>