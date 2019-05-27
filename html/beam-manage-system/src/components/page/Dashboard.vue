<template>
    <div>
        <el-row :gutter="20">
            <el-col :span="8">
                <el-card shadow="hover" class="mgb20" style="height:252px;">
                    <div class="user-info">
                        <img :src="sysuser.avatar" class="user-avator" alt="">
                        <div class="user-info-cont">
                            <div class="user-info-name">{{sysuser.account}}</div>
                            <div>{{sysuser.roleNames[0]}}</div>
                        </div>
                    </div>
                    <div class="user-info-list">真实姓名：<span>{{sysuser.name}}</span></div>
                    <div class="user-info-list">所在部门：<span>{{sysuser.deptName}}</span></div>
                </el-card>
                <el-card shadow="hover" style="height:400px;">
                    <div slot="header" class="clearfix">
                        <span>客户状态百分比</span>
                    </div>
                    客户未知
                    <el-progress :percentage="CommEntity.unknownRate" color="#42b983"></el-progress>
                    <br/> <br/>
                    客户拜访
                    <el-progress :percentage="CommEntity.viewRate" color="#42b983"></el-progress>
                    <br/> <br/>
                    客户意向
                    <el-progress :percentage="CommEntity.intentionRate" color="#f1e05a"></el-progress>
                    <br/> <br/>
                    客户签约
                    <el-progress :percentage="CommEntity.contractRate"></el-progress>
                </el-card>

            </el-col>
            <el-col :span="16">
                <el-row :gutter="20" class="mgb20">
                    <el-col :span="8">

                        <el-card shadow="hover" :body-style="{padding: '0px'}">
                            <div class="grid-content grid-con-1">
                                <i class="el-icon-lx-people grid-con-icon"></i>
                                <div class="grid-cont-right">
                                    <div class="grid-num">{{CommEntity.count}}</div>
                                    <div>未拜访客户</div>
                                </div>
                            </div>
                        </el-card>
                    </el-col>
                    <el-col :span="8">
                        <el-card shadow="hover" :body-style="{padding: '0px'}">
                            <div class="grid-content grid-con-2">
                                <i class="el-icon-lx-people grid-con-icon"></i>
                                <div class="grid-cont-right">
                                    <div class="grid-num">{{CommEntity.myTotal}}</div>
                                    <div>我的客户</div>
                                </div>
                            </div>
                        </el-card>
                    </el-col>
                    <el-col :span="8">
                        <el-card shadow="hover" :body-style="{padding: '0px'}">
                            <div class="grid-content grid-con-3">
                                <i class="el-icon-lx-people grid-con-icon"></i>
                                <div class="grid-cont-right">
                                    <div class="grid-num">{{CommEntity.total}}</div>
                                    <div>总客户数</div>
                                </div>
                            </div>
                        </el-card>
                    </el-col>
                </el-row>
                <el-card shadow="hover" style="height:600px;">
                    <div slot="header" class="clearfix">
                        <span>待拜访客户</span>
                    </div>
                    <el-table row-key="id" :data="tableData" v-loading = "loading" border class="table" ref="multipleTable"  @selection-change="handleSelectionChange">
                        <el-table-column  label="客户ID" align="center" prop="id" v-if="show">
                        </el-table-column>
                        <el-table-column label="客户店名" align="center" prop="customerStoreName">
                        </el-table-column>
                        <el-table-column label="联系人姓名" align="center" prop="customerContactName">
                        </el-table-column>
                        <el-table-column label="联系人电话" align="center" prop="customerPhone">
                        </el-table-column>
                    </el-table>
                    <div class="pagination">
                        <el-pagination
                            background
                            :page-sizes="[10, 20, 30, 40, 50]"
                            :page-size="page.pageSize"
                            :current-page="page.pageNo"
                            @current-change="handleCurrentChange"
                            @size-change="changePageSize"
                            layout="prev, pager, next"
                            :total="page.totalRows">
                        </el-pagination>
                    </div>
                </el-card>
            </el-col>

        </el-row>


    </div>
</template>

<script>
    import Schart from 'vue-schart';
    import bus from '../common/bus';
    import DashboardApi from '../../api/dashboard';

    export default {
        name: 'dashboard',
        data() {
            return {
                user : null,
                CommEntity:null,
                tableData: [],
                page: {pageNo: 1, pageSize: 10},
                loading: false,
                show:false,
                req: {},
            }
        },
        components: {
            Schart
        },
        computed: {
            sysuser(){
                let sysuser = JSON.parse(localStorage.getItem('sysuser'));
                console.log(sysuser.account);
                console.log(sysuser.avatar);
                return sysuser?sysuser:this.user;
            }
        },
        created(){
            this.getDashboardContent();
            this.handleListener();
            this.getTotal();
            this.getData();
        },
        activated(){
            this.handleListener();
        },
        deactivated(){
            window.removeEventListener('resize', this.renderChart);
            bus.$off('collapse', this.handleBus);
        },
        methods: {
            getTotal() {
                DashboardApi.getCustomerTotal().then((res) => {
                    if (res.error === false) {
                        this.CommEntity = res.data;

                    } else {
                        this.$message.error(res.msg);
                    }
                }, (err) => {
                    this.$message.error(err.msg);
                });
            },
            // 获取 easy-mock 的模拟数据
            getData() {
                this.loading = true;
                this.req.currentPage = this.page.pageNo
                this.req.pageSize = this.page.pageSize
                DashboardApi.getData(this.req).then((res) => {
                    this.loading = false;
                    if (res.error === false) {
                        this.tableData = res.data.records ? res.data.records : []
                        this.page.pageNo = parseInt(res.data.current)
                        this.page.totalRows = parseInt(res.data.total)
                        this.tableData.forEach(item => {
                            item.status = Boolean(item.status)
                        })
                    } else {
                        this.$message.error(res.msg);
                    }
                }, (err) => {
                    this.loading = false;
                    this.$message.error(err.msg);
                });
            },
            getDashboardContent(){
                DashboardApi.getDashboardContent().then((res)=>{
                    console.log(res);
                },(err) => {
                    console.log(err)
                    this.$message.error(err.msg);
                })
            },

            handleListener(){
                bus.$on('collapse', this.handleBus);
                // 调用renderChart方法对图表进行重新渲染
                window.addEventListener('resize', this.renderChart)
            },
            handleBus(msg){
                setTimeout(() => {
                    this.renderChart()
                }, 300);
            },
            handleCurrentChange(val) {
                this.page.pageNo = val;
                this.getData();
            },
            changePageSize(value) { // 修改每页条数size
                this.page.pageNo = 1
                this.page.pageSize = value
                this.tableData = null
                this.getData()
            },
        }
    }

</script>


<style scoped>
    .el-row {
        margin-bottom: 20px;
    }

    .grid-content {
        display: flex;
        align-items: center;
        height: 100px;
    }

    .grid-cont-right {
        flex: 1;
        text-align: center;
        font-size: 14px;
        color: #999;
    }

    .grid-num {
        font-size: 30px;
        font-weight: bold;
    }

    .grid-con-icon {
        font-size: 50px;
        width: 100px;
        height: 100px;
        text-align: center;
        line-height: 100px;
        color: #fff;
    }

    .grid-con-1 .grid-con-icon {
        background: rgb(242, 94, 67);
    }

    .grid-con-1 .grid-num {
        color: rgb(242, 94, 67);
    }

    .grid-con-2 .grid-con-icon {
        background: rgb(100, 213, 114);
    }

    .grid-con-2 .grid-num {
        color: rgb(45, 140, 240);
    }
    .grid-con-3 .grid-con-icon {
        background: rgb(45, 140, 240);
    }
    .grid-con-3 .grid-num {
        color: rgb(45, 140, 240);
    }

    .user-info {
        display: flex;
        align-items: center;
        padding-bottom: 20px;
        border-bottom: 2px solid #ccc;
        margin-bottom: 20px;
    }

    .user-avator {
        width: 120px;
        height: 120px;
        border-radius: 50%;
    }

    .user-info-cont {
        padding-left: 50px;
        flex: 1;
        font-size: 14px;
        color: #999;
    }

    .user-info-cont div:first-child {
        font-size: 30px;
        color: #222;
    }

    .user-info-list {
        font-size: 14px;
        color: #999;
        line-height: 25px;
    }

    .user-info-list span {
        margin-left: 70px;
    }

    .mgb20 {
        margin-bottom: 20px;
    }

    .todo-item {
        font-size: 14px;
    }

    .todo-item-del {
        text-decoration: line-through;
        color: #999;
    }

    .schart {
        width: 100%;
        height: 300px;
    }
    .table {
        width: 100%;
        font-size: 14px;
    }

</style>
