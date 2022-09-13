<!--
 * @Author: your name
 * @Date: 2021-05-07 10:05:31
 * @LastEditTime: 2022-02-24 13:56:04
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \fe_framework_template\src\views\layout\components\Sidebar\index.vue
-->
<template>
  <div>
    <div class="dv-togger">
      <hamburger
        class="hamburger-container"
        :toggleClick="toggleSideBar"
        :isActive="sidebar.opened"
      ></hamburger>
    </div>
    <el-scrollbar wrapClass="scrollbar-wrapper">
      <el-menu
        mode="vertical"
        show-timeout="200"
        unique-opened
        :default-active="onRoutes"
        :collapse="isCollapse"
        class="sider-menu"
        :collapse-transition="false"
        disabled
      >
        <!-- <sidebar-item :routes="routes"></sidebar-item> -->
        <!-- <SidebarItem :menu-list="menu"  v-if="menu.length" /> -->
        <template v-if="menu.length"> 
          <SidebarItem   v-for="route in menu" :key="route.path" :item="route" :base-path="route.path" />
        </template>
        
      </el-menu>
    </el-scrollbar>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import SidebarItem from "./SidebarItem";
import Hamburger from "@/components/hamburger";
import { set } from "nprogress";

export default {
  components: {
    SidebarItem,
    Hamburger,
  },
  data() {
    return {
      menu: [],
    };
  },
  name: "sidebar",
  watch: {
    menuList:{
        handler(menuList) {
            if (Array.isArray(menuList) && menuList.length) {
                this.menuListFn(menuList);
            }
        },
        deep: true,
        immediate: true
    },
  },
  mounted() {
    // setTimeout(() => console.log(this.menu, 'menu'), 2000)
    // ;
  },
  computed: {
    ...mapGetters(["sidebar", "menuList"]),
    isCollapse() {
      return !this.sidebar.opened;
    },
    onRoutes() {
      return this.$route.meta.path ? this.$route.meta.path : this.$route.path;
    },
  },
  methods: {
    toggleSideBar() {
      this.$store.dispatch("ToggleSideBar");
    },

    menuListFn(menuList) {
      const menu = this.deleteComponent(menuList);
      setTimeout(() => (this.menu = menu));
    },
    /* 去除不需要的参数 */
    deleteComponent(menuList) {
      return menuList.map((item) => {
        const i = JSON.parse(JSON.stringify(item));
        if (i.component) delete i.component;
        if (!i?.children?.length) {
          delete i.children;
          return i;
        }

        i.children = this.deleteComponent(i.children);
        return i;
      });
    },
  },
};
</script>
