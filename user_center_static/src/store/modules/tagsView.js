const state = {
    visitedViews: [
        {
            path: '/home',
            name: 'home',
            fullPath: '/home',
            meta: {tagTitle: '首页', deepth: 1, noCache: true, affix: true},
        }
    ],
    cachedViews: [],
    visitedTag: new Map(), //记录访问过得页签 

}
const mutations = {

    ADD_VISITED_VIEW: (state, view) => {
        
        if (state.visitedViews.some(v => v.name === view.name)) {
            return;
        }
       
        state.visitedTag.set(view.path, view.name)
        state.visitedViews.push(
            Object.assign({}, view, {
                title: view.meta.title || '404页面'
            })
        )
    },
    ADD_CACHED_VIEW: (state, view) => {
        if (state.cachedViews.includes(view.name)) return;
        if (!view.meta.noCache) {
            state.cachedViews.push(view.name)
        }
    },
    DEL_VISITED_VIEW: (state, view) => {
        /**
         * 删除所有name相同的访问记录
         */
         mutations.DEL_VISITED_TAG(state, view.name)

        for (const [i, v] of state.visitedViews.entries()) {
            
            if (v.name === view.name) {
                view.meta.noLoading = false
                state.visitedViews.splice(i, 1)
                break
            }
        }
    },
    DEL_CACHED_VIEW: (state, view) => {
        const index = state.cachedViews.indexOf(view.name);
        
        if (index > -1) {
            
            state.cachedViews.splice(index, 1)
            view.meta.noLoading = false
        }
    },
    DEL_VISITED_TAG: (state, name) => {
        let map = new Map();
        for(let[key, value] of state.visitedTag) {
            name != value && map.set(key, value)
        }
        state.visitedTag = new Map(map);
    },
    DEL_OTHERS_VISITED_VIEWS: (state, {view, type}) => {
        const index = type !== 'other' ? state.visitedViews.indexOf(state.visitedViews.find(item => {
            return item.name === view.name
        })) : -1
        let visitedViews = [];
        // state.visitedViews = state.visitedViews.filter((v, i) => {
            
        //     if (type === 'other') return v.meta.affix || v.name === view.name
        //     if (type === 'right') return v.meta.affix || i <= index
        //     if (type === 'left') return v.meta.affix || i >= index
           
        // })
        state.visitedViews.forEach((v, i) => {
            if (type === 'other') {
                if (v.meta.affix || v.name === view.name || v.meta.temAffix) {
                    visitedViews.push(v);
                }else {
                    /**
                     * 删除所有name相同的访问记录
                     */
                    mutations.DEL_VISITED_TAG(state, v.name)
                    v.meta.noLoading = false;
                }
            }
            if (type === 'right') {
                if (v.meta.affix || v.meta.temAffix || i <= index) {
                    visitedViews.push(v);
                    return
                }else {
                    /**
                     * 删除所有name相同的访问记录
                     */
                    mutations.DEL_VISITED_TAG(state, v.name)
                    v.meta.noLoading = false;
                }
            }
            if (type === 'left') {
                if (v.meta.affix || v.meta.temAffix || i >= index) {
                    visitedViews.push(v);
                    return
                }else {
                    /**
                     * 删除所有name相同的访问记录
                     */
                    mutations.DEL_VISITED_TAG(state, v.name)
                    v.meta.noLoading = false;
                }
            }
        })
        state.visitedViews = visitedViews;
    },
    DEL_OTHERS_CACHED_VIEWS: (state, {view, type}) => {
        const index = state.cachedViews.indexOf(view.name)
        if (index > -1) {
            if (type === 'other') state.cachedViews = state.cachedViews.slice(index, index + 1)
            if (type === 'right') state.cachedViews = state.cachedViews.slice(0, index + 1)
            if (type === 'left') state.cachedViews = state.cachedViews.slice(index, state.cachedViews.length)

        } else {
            state.cachedViews = []
        }
    },
    DEL_ALL_VISITED_VIEWS: state => {
        const affixTags = state.visitedViews.filter(tag => {
            if (tag.meta.affix) {
                return tag.meta.affix || tag.meta.temAffix
            }
            tag.meta.noLoading = false
            /**
             * 清除访问页签记录
             */
            state.visitedTag.clear();
        })
        state.visitedViews = affixTags
    },
    DEL_ALL_CACHED_VIEWS: state => {
        state.cachedViews = []
    },
    UPDATE_VISITED_VIEW: (state, view) => {
        for (let v of state.visitedViews) {
             /**
             * 如果访问过该页签，则去除等待层
             */
            view.meta.noLoading = state.visitedTag.get(view.path) ? true : false;
            
            if (v.name === view.name) {
                v = Object.assign(v, view);
                state.visitedTag.set(view.path, view.name)
                break
            }
        }
    }
}
const actions = {
    addView({dispatch}, view) {
        dispatch('addVisitedView', view)
        dispatch('addCachedView', view)
    },
    addVisitedView({commit}, view) {
        commit('ADD_VISITED_VIEW', view)
    },
    addCachedView({commit}, view) {
        commit('ADD_CACHED_VIEW', view)
    },
    delVisitedTag({commit}, name) {
        commit('DEL_VISITED_TAG', name)
    },
    delView({dispatch, state}, view) {
        view.meta && view.meta.from && (delete view.meta.from);
        return new Promise(resolve => {
            dispatch('delVisitedView', view)
            dispatch('delCachedView', view)
            resolve({
                visitedViews: [...state.visitedViews],
                cachedViews: [...state.cachedViews]
            })
        })
    },
    delVisitedView({commit, state}, view) {
        return new Promise(resolve => {
            commit('DEL_VISITED_VIEW', view)
            resolve([...state.visitedViews])
        })
    },
    delCachedView({commit, state}, view) {
        return new Promise(resolve => {
            commit('DEL_CACHED_VIEW', view)
            resolve([...state.cachedViews])
        })
    },
    delOthersViews({dispatch, state}, {view, type}) {
        return new Promise(resolve => {
            dispatch('delOthersVisitedViews', {view, type})
            dispatch('delOthersCachedViews', {view, type})
            resolve({
                visitedViews: [...state.visitedViews],
                cachedViews: [...state.cachedViews]
            })
        })
    },
    delOthersVisitedViews({commit, state}, {view, type}) {
        return new Promise(resolve => {
            commit('DEL_OTHERS_VISITED_VIEWS', {view, type})
            resolve([...state.visitedViews])
        })
    },
    delOthersCachedViews({commit, state}, {view, type}) {
        return new Promise(resolve => {
            commit('DEL_OTHERS_CACHED_VIEWS', {view, type})
            resolve([...state.cachedViews])
        })
    },
    delAllViews({dispatch, state}, view) {
        return new Promise(resolve => {
            dispatch('delAllVisitedViews', view)
            dispatch('delAllCachedViews', view)
            resolve({
                visitedViews: [...state.visitedViews],
                cachedViews: [...state.cachedViews]
            })
        })
    },
    delAllVisitedViews({commit, state}) {
        return new Promise(resolve => {
            commit('DEL_ALL_VISITED_VIEWS')
            resolve([...state.visitedViews])
        })
    },
    delAllCachedViews({commit, state}) {
        return new Promise(resolve => {
            commit('DEL_ALL_CACHED_VIEWS')
            resolve([...state.cachedViews])
        })
    },
    updateVisitedView({commit}, view) {
        commit('UPDATE_VISITED_VIEW', view)
    }
}
export default {
    namespaced: true,
    state,
    mutations,
    actions,
}
