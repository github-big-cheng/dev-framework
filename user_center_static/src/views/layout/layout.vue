<template>
  <div class="app-box">
    <Header v-if="isLogin"></Header>
    <div class="app-wrapper" :class="classObj" v-if="isLogin">
      <div v-if="device === 'mobile' && sidebar.opened" @click="handleClickOutside"></div>
      <sidebar class="sidebar-container"></sidebar>
      <div class="main-container">
        <tags-view  v-if="needTagsView"></tags-view>
        <app-main></app-main>
        <span v-html="loadingStyle"></span>
      </div>
      <!-- <right-panel>
        <settings />
      </right-panel> -->
      <modify-password v-if="isLogin"></modify-password>
    </div>
  </div>
</template>

<script>
import { AppMain, Header, Sidebar, TagsView, Settings, ModifyPassword } from './components';
import { getLocalStorage } from '@/utils/auth';
// import RightPanel from '@/components/right-panel'

export default {
  name: 'layout',
  components: {
    Sidebar,
    AppMain,
    Header,
    ModifyPassword,
    TagsView
    // Settings,
    // RightPanel
  },
  data() {
    return {
      inRoute: '',
      isLogin: false,
      isTestingInput: false,
      firstLogin: '',
    };
  },
  watch: {
    $route: {
      handler(newRouter) {
        this.inRoute = newRouter.path;
      },
    },
  },
  mounted() {
    this.isLogin = !!getLocalStorage('userInfo');
  },
  computed: {
    sidebar() {
      return this.$store.state.app.sidebar;
    },
    needTagsView() {
      return this.$store.state.settings.tagsView
    },
    device() {
      return this.$store.state.app.device;
    },
    classObj() {
      return {
        hideSidebar: !this.sidebar.opened,
        withoutAnimation: this.sidebar.withoutAnimation,
        mobile: this.device === 'mobile',
      };
    },
    loadingStyle() {
      return `<style>
         
          ._loading>.el-loading-mask {
            height: calc(100% - 32px);
            top: 32px;
          }
      </style>`;
    },
  },
  methods: {
    handleClickOutside() {
      this.$store.dispatch('CloseSideBar', { withoutAnimation: false });
    },
  },
};
</script>

<style lang="scss" scoped>
@import 'src/styles/mixin.scss';

.app-box {
  height: 100%;
}

.app-wrapper {
  @include clearfix;
  position: relative;
  height: 100%;
  width: 100%;
}

</style>
