<template>
  <el-dialog v-model="visible" title="查看任务">
    <template v-for="item in info" :key="item.filename">
      <el-descriptions :title="item.filename.split('.')[0]"
                       border :column="4">
        <template #extra>
          <el-button type="danger" @click="deleteTask(item)">删除</el-button>
        </template>
        <el-descriptions-item label="标题">
          {{ item.title }}
        </el-descriptions-item>
      </el-descriptions>
    </template>
  </el-dialog>
</template>

<script>
import {listByDates} from "@/modules/WorkCenter/api/WorkCenterAPI";

export default {
  name: "DailyTaskDialog",
  data() {
    return {
      visible: false,
      info: []
    }
  },
  mounted() {
    listByDates(new Date("2023-01-25").getTime())
        .then((data) => {
          this.info = data.data;
        })
        .catch((error) => {
          console.log(error)
        })
  },
  methods: {
    showDialog() {
      this.visible = true;
      this.$emit("showDialog");
    },
    closeDialog(emitData = {}) {
      this.visible = false;
      this.$emit("closeDialog", emitData)
    },
    deleteTask(item) {
      //TODO 删除任务
      console.log(" TODO delete...", item)
    }
  },
  props: {
    date: {
      type: Date,
      required: true
    }
  }
}
</script>