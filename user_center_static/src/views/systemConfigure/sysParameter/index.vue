<template>
    <div>1</div>
</template>

<script>
export default {
    name: 'parameterList',
    data() {
        return {
            
        }
    },
    created() {
        this.$http.getUcenterRoleList()
    }
}
</script>