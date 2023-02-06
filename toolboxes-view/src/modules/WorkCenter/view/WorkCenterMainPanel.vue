<template>
  <div id="work-center-main-panel">
    <el-calendar v-model="selectDate">
      <template #date-cell="{ data }">
        <div>
          {{ data.day.split('-').slice(1).join('-') }}
          {{ data.isSelected ? '✔️' : '' }}
          <el-tag v-show="data.isSelected"
                  @click="showTasks(data.date,todayTaskCount)"
                  :type="todayTaskCount==0?'info':'success'"
                  round effect="dark">
            {{ todayTaskCount }}
          </el-tag>
        </div>
      </template>
    </el-calendar>
    <daily-task-dialog ref="view-dialog" :date="selectDate"/>
  </div>
</template>

<script>
import {listDateTaskCount} from "@/modules/WorkCenter/api/WorkCenterAPI";
import DailyTaskDialog from "@/modules/WorkCenter/view/DailyTaskDialog";

export default {
  name: "WorkCenterMainPanel",
  components: {DailyTaskDialog},
  data() {
    return {
      info: {},
      selectDate: new Date(),
      todayTaskCount: 0
    }
  },
  methods: {
    showTasks(date, count = 0) {
      if (count == 0) {
        return;
      }
      this.$refs['view-dialog'].showDialog();
    },
    newTask() {

    }
  },
  watch: {
    selectDate(val) {
      listDateTaskCount(val.getTime()).then(data => {
        this.todayTaskCount = data.data;
      }).catch(() => {
        this.todayTaskCount = 0
      })
    }
  }
}
</script>