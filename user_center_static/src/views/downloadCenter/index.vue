<template>
    <div class="download-center">
        <div class="manual-download">
            <pageTitle title="操作手册下载"/>
            <ul class="manual-list">
                <li v-for="item in downloadData.systemManuals" :key="item.id">
                    <a :href="url + '/file' + item.value" class="file-download" target="_blank" :download="item.descript">
                        <i
                            class="el-icon-aliword"
                            v-if="item.disType == 7 || item.disType == 'docx'"
                        ></i>
                        <i
                            class="el-icon-alipdf"
                            v-else-if="item.disType == 'pdf'"
                        ></i>
                        <i
                            class="el-icon-alippt"
                            v-else-if="item.disType == 'ppt' || item.disType == 'pptx'"
                        ></i>
                        <i
                            class="el-icon-aliexcel"
                            v-else-if="item.disType == 'xls' || item.disType == 'xlsx'"
                        ></i>
                        <i
                            class="el-icon-alipic"
                            v-else-if="item.disType == 'jpg' || item.disType == 'png'"
                        ></i>
                        <i class="el-icon-aliother" v-else></i>
                        <span>{{item.descript}}</span>
                    </a>
                </li>
            </ul>
        </div>
    </div>
</template>

<script>
import pageTitle from "@/components/page-title"
import Api, {requestUrl} from "@/api/api";
import VueQr from 'vue-qr';
export default {
    name: "downloadCenter",
    components: {
        pageTitle,
        VueQr
    },
    data() {
        return {
            BASE_URL: requestUrl,
            downloadData :
                {
                    systemManuals:[],
                }
            ,
            url: '',
        }
    },
    created() {
        this.url = requestUrl;
        this.getDownloadList();
    },
    methods:{
         getDownloadList() {
          Api.getUcenterDownloadList({}).then((res) => {
            this.closeLoading(this.$route);
            if (res.code == 0) {
              console.log(res.data);
              Object.assign(this.downloadData, res.data);
            }
          }).catch((err) => {
            console.log(err);
            this.closeLoading(this.$route);
          })
        },
    }
}
</script>

<style lang="scss" scoped>
.download-center{
    padding: 0 .5rem;
}
.program-download,
.manual-download {
    padding: 30px 0 0;

    h2 {
        padding-bottom: 10px;
        font-size: 16px;
    }
}
.qrcode-dl{padding-right: 20px;
    img{
        width: 84px;
        height: 84px;
        padding: 5px;
        border: 1px solid #2196f3;
        border-radius: 5px;
    }
    p{
        text-align: center;
        padding-top: 5px;
        color:#2196f3;
    }
}
.program-download {
    h2 {
        padding-bottom: 25px
    }
    .bd{
        display: flex;
    }
    ul{
        padding-top: 10px
    }
}

.program-list {
    li {
        position: relative;
        float: left;
        width: 100px;
        height: 104px;
        margin-bottom: 10px;
        margin-right: 10px;
        text-align: center;

        a {
            display: block !important;
            color: #2196f3
        }

        .icon-drivers {
            display: block;
            margin: 5px auto 0;
            font-size: 28px;
            width:66px;
            height: 66px;
            border-radius: 100%;
            color: #fff;
            background-color: #8f93ed;
            position: relative;
            font-weight: normal;
            i {
                position: absolute;
                transform: translate(-50%, -50%);
                top: 50%;
                left: 50%;
                font-size: 32px;
                color: #fff;
            }
        }

        span {
            display: block;
            padding-top: 17px;
            margin: 0;
            line-height: 1.2;
        }

        &:nth-child(5n+1) label {
            background-color: #f3c436;
        }


        &:nth-child(5n+2) label {
            background-color: #8f92ed;
        }


        &:nth-child(5n+3) label {
            background-color: #5791e9;
        }

        &:nth-child(5n+4) label {
            background-color: #da4127;
        }

        &:nth-child(5n+5) label {
            background-color: #1add91;
        }
    }
}
.manual-download {
    width: 80%;
}
.qrcode-dl img {
    vertical-align: middle;
}
.program-download ul {
    padding-top: 0;
    .program-list li {
        margin: 0;
    }
}

.manual-list {
    li {
        height: 23px;
        margin-top: 10px;

        a {
            color: #2196f3;

            i {
                font-size: 18px;
                vertical-align: middle;
                padding-right: 5px;
            }

            &:hover {
                color: #2196f3;
                i{ color: #2196f3;}
                span{
                    text-decoration: underline;
                }
            }
        }
    }
}
</style>
