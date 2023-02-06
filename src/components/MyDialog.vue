<template>
  <el-dialog v-model="visible" :title="title">

  </el-dialog>
</template>

<script>
export default {
  name: "MyDialog",
  data() {
    return {
      visible: false
    }
  },
  methods: {
    showDialog() {
      this.visible = true;
      this.$emit("showDialog");
    },
    closeDialog(emitData = {}) {
      this.visible = false;
      this.$emit("closeDialog", emitData)
    }
  },
  props: {
    title: {
      type: String,
      required: true
    },
  }
}
</script>