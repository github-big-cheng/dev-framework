<template>
    <div class="main-content-wrap inner-maincon">
        <ViewCom :view-configs="viewConfig" />
    </div>
</template>

<script>
import ViewCom from "@/components/view-com/index.vue";
import { viewConfig } from "./config/view";
export default {
    name: "flowDefineView",
    components: {
        ViewCom,
    },
    data() {
        return {
            viewConfig: viewConfig(),
        };
    },
    mounted() {
        const { id } = this.$route.params;
        this.requestView(id);
    },
    methods: {
        async requestView(id) {
            try {
                const { data } = await this.$http.flowDefineView({ id });
                this.viewConfig = viewConfig(data);
            } catch (error) {}
        },
    },
};
</script>
