<template>
  <el-container id="home-page" style="width: 100%;height: 100%">
    <el-aside id="home-page-aside">
      <el-menu
          router
          :default-active="activeIndex"
          mode="vertical"
      >
        <el-menu-item :index="item.path" :key="item.path" v-for="item of menus">
          <span>{{ item.name }}</span>
        </el-menu-item>
      </el-menu>
    </el-aside>
    <!--    <el-container id="home-page-main">-->
    <!--      <el-header>-->

    <!--      </el-header>-->
    <el-main style="width: 200px;height: 100%">
      <router-view></router-view>
    </el-main>
    <!--    </el-container>-->
  </el-container>
</template>

<script>
export default {
  name: "HomePage",
  data() {
    return {
      menus: [],
      activeIndex: ''
    }
  },
  mounted() {
    this.menus = this.$context.module_name_zh;
    console.log(this.menus)
  }
}
</script>