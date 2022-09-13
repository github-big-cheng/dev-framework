<template>
    <div id="tags-view-container" ref="tagsViewContainer" class="tags-view-container">
        <div class="tags-view-left" v-show="show" @click="onCcrollPane('left')">
            <i class="el-icon-d-arrow-left"></i>
        </div>
        <scroll-pane ref="scrollPane" class="tags-view-wrapper">
            <router-link v-for="(tag, index) in visitedViews" ref="tag" :key="tag.path"
                         :class="isActive(tag)?'active':''"
                         :to="{ path: tag.path, query: tag.query, fullPath: tag.fullPath }" tag="span"
                         class="tags-view-item"
                         @click.middle.native="!isAffix(tag)?closeSelectedTag(tag):''"
                         @contextmenu.prevent.native="openMenu(tag,index,$event)">
                {{ tag.meta.tagTitle || tag.title }}
                <span v-if="!isAffix(tag)" :class="[tag.meta.tagTitle !== '首页' ? 'el-icon-close' : null]"
                    @click.prevent.stop="closeSelectedTag(tag)"/>
                <span v-if="isTemAffix(tag)" class="fix-tag">
                    <i class="el-icon-aliguding"></i>
                </span>
                
            </router-link>
        </scroll-pane>
        <div class="tags-view-right" v-show="show" @click="onCcrollPane('right')">
            <i class="el-icon-d-arrow-right"></i>
        </div>
        <ul v-show="visible" :style="{left:left+'px',top:top+'px'}" class="tags-view-contextmenu">
            <!-- <li @click="refreshSelectedTag(selectedTag)">刷新</li> -->
            <!-- <li @click="affixTag(selectedTag)">{{selectedTag.meta && selectedTag.meta.temAffix?'取消固定' : '固定标签'}}</li> -->
            <li v-if="!isAffix(selectedTag)" @click="closeSelectedTag(selectedTag)">关闭</li>
            <li @click="closeAllTags(selectedTag)">关闭全部</li>
            <li @click="closeOthersTags('other')">关闭其它</li>
            <li @click="closeOthersTags('right')">关闭右侧标签</li>
            <li @click="closeOthersTags('left')">关闭左侧标签</li>
        </ul>
    </div>
</template>

<script>
    import ScrollPane from './ScrollPane'
    import path from 'path'

    export default {
        components: {
            ScrollPane
        },
        data() {
            return {
                visible: false,
                top: 0,
                left: 0,
                selectedTag: {},
                affixTags: [],
                selectedIndex: null,
                show: false
            }
        },
        computed: {
            visitedViews() {
                return this.$store.state.tagsView.visitedViews
            },
            routes() {
                return this.$store.state.user.menuList
            },
            loading() {
                return !this.$route.meta.noLoading && !this.$route.meta.hasOwnProperty('noWait');
            }
        },
        watch: {
            $route() {
                this.addTags()
                this.moveToCurrentTag()
            },
            visible(value) {
                if (value) {
                    document.body.addEventListener('click', this.closeMenu)
                } else {
                    document.body.removeEventListener('click', this.closeMenu)
                }
            }
        },
        mounted() {
            this.initTags()
            this.addTags()
        },
        methods: {
            affixTag(tag) {
                tag.meta.temAffix = !tag.meta.temAffix;
            },
            isActive(route) {
                return route.path === this.$route.path
            },
            isAffix(tag) {
                return tag.meta && tag.meta.affix
            },
            isTemAffix(tag) {
                return tag.meta && tag.meta.temAffix
            },
            filterAffixTags(routes, basePath = '/') {

                if (!routes) return [];

                let tags = []
                routes.forEach(route => {
                    if (route.meta && route.meta.affix) {
                        const tagPath = path.resolve(basePath, route.path)
                        tags.push({
                            fullPath: tagPath,
                            path: tagPath,
                            name: route.name,
                            meta: {
                                ...route.meta
                            }
                        })
                    }
                    if (route.children) {
                        const tempTags = this.filterAffixTags(route.children, route.path)
                        if (tempTags.length >= 1) {
                            tags = [...tags, ...tempTags]
                        }
                    }
                })
                return tags
            },
            initTags() {
                const affixTags = (this.affixTags = this.filterAffixTags(this.routes))
                for (const tag of affixTags) {
                    if (tag.name) {
                        this.$store.dispatch('tagsView/addVisitedView', tag)
                    }
                }
            },
            addTags() {
                const {name} = this.$route
                if (name) {
                    this.$store.dispatch('tagsView/addView', this.$route)
                    this.setShow()
                }
                return false
            },
            moveToCurrentTag() {
                this.$nextTick(() => {
                    const tags = this.$refs.tag
                    for (const tag of tags) {
                        if (tag.to.path === this.$route.path) {
                            this.$refs.scrollPane.moveToTarget(tag)
                            if (tag.to.fullPath !== this.$route.fullPath) {
                                this.$store.dispatch('tagsView/updateVisitedView', this.$route)
                            }
                            break
                        }
                    }
                })
            },
            // refreshSelectedTag (view) {
            //   this.$store.dispatch('tagsView/delCachedView', view).then(() => {
            //     const { fullPath } = view
            //     this.$nextTick(() => {
            //       this.$router.replace({
            //         path: '/redirect' + fullPath
            //       })
            //     })
            //   })
            // },
            closeSelectedTag(view) {
                this.$store.dispatch('tagsView/delView', view).then(({visitedViews}) => {
                    this.setShow()
                    if (this.isActive(view)) {
                        this.toLastView(visitedViews, view)
                    }
                })
            },
            closeOthersTags(type) {
                this.$router.push(this.selectedTag)
                this.$store.dispatch('tagsView/delOthersViews', {view: this.selectedTag, type: type}).then(() => {
                    this.setShow()
                    this.moveToCurrentTag()
                })
            },
            closeAllTags(view) {
                this.$store.dispatch('tagsView/delAllViews').then(({visitedViews}) => {
                    this.show = false
                    if (this.affixTags.some(tag => tag.path === view.path)) {
                        return
                    }
                    this.toLastView(visitedViews, view)
                })
            },
            toLastView(visitedViews, view) {
                const latestView = visitedViews.slice(-1)[0]
                if (latestView) {
                    this.$router.push(latestView.fullPath)
                } else {
                    this.$router.push('/')
                }
            },
            openMenu(tag, index, e) {
                const menuMinWidth = 105
                const offsetLeft = this.$el.getBoundingClientRect().left
                const offsetWidth = this.$el.offsetWidth
                const maxLeft = offsetWidth - menuMinWidth
                const left = e.clientX - offsetLeft + 15
                if (left > maxLeft) {
                    this.left = maxLeft
                } else {
                    this.left = left
                }
                this.top = e.clientY - this.$el.getBoundingClientRect().top
                this.visible = true
                this.selectedTag = tag
                this.selectedIndex = index
                // this.left = e.clientX
                // this.top = e.clientY - this.$el.getBoundingClientRect().top
                // this.visible = true
                // this.selectedTag = tag
                // this.selectedIndex = index
            },
            closeMenu() {
                this.visible = false
            },
            setShow() {
                const tags = this.$refs.tag
                if (tags) {
                    const maxWidth = this.$refs.tagsViewContainer.offsetWidth
                    let tagsAllWidth = 0
                    tags.forEach(e => {
                        tagsAllWidth += e.$el.offsetWidth
                    });
                    this.show = tagsAllWidth >= maxWidth - 56//120
                }
            },
            onCcrollPane(type) {
                this.$refs.scrollPane.scrollLeftOrRight(type)
            }
        }
    }
</script>
<style lang="scss" scoped>
    @import "@/styles/tags-view.scss";
</style>
