<template>
    <div class="table">
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item><i class="el-icon-lx-people"></i>签约报表</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="container">
            <el-container>
                <el-container>
                    <el-header>
                        <el-input style="width: 120px" v-model="req.customerStoreName" placeholder="客户名称"></el-input>
                        <el-date-picker type="date" id="startTime" placeholder="开始时间" v-model="reqExport.startTime"
                                        value-format="yyyy-MM-dd">
                        </el-date-picker>
                        <el-date-picker type="date" id="endTime" placeholder="结束时间" v-model="reqExport.endTime"
                                        value-format="yyyy-MM-dd">
                        </el-date-picker>
                        <el-button type="primary" icon="search" @click="search">搜索</el-button>
                        <el-button @click="handleExport" size="mini" icon="el-icon-download">导出数据</el-button>
                    </el-header>
                    <el-main>
                        <el-table :data="tableData" v-loading="loading" border class="table" ref="multipleTable"
                                  @selection-change="handleSelectionChange">
                            <el-table-column type="selection" width="55" align="center"></el-table-column>
                            <el-table-column label="客户ID" align="center" prop="id" v-if="false">
                            </el-table-column>
                            <el-table-column label="客户编码" align="center" prop="customerId">
                            </el-table-column>
                            <el-table-column label="客户店名" align="center" prop="customerStoreName">
                            </el-table-column>
                            <el-table-column prop="visitTime" align="center" label="签约时间">
                            </el-table-column>
                            <el-table-column prop="userName" align="center" label="销售">
                            </el-table-column>
                            <el-table-column label="签约金额" align="center" prop="amount">
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
                    </el-main>
                </el-container>
            </el-container>


        </div>
    </div>
</template>
<style>
    .avatar-uploader .el-upload {
        border: 1px dashed #d9d9d9;
        border-radius: 6px;
        cursor: pointer;
        position: relative;
        overflow: hidden;
        width: 100px;
        height: 100px;
    }

    .avatar-uploader .el-upload:hover {
        border-color: #409EFF;
    }

    .avatar-uploader-icon {
        font-size: 28px;
        color: #8c939d;
        width: 100px;
        height: 100px;
        line-height: 100px;
        text-align: center;
    }

    .avatar {
        width: 100px;
        height: 100px;
        display: block;
    }

    .warning {
        color: #E6A23C;
    }

    .red {
        color: #ff0000;
    }
</style>
<script>
    import SigningReportApi from '../../api/signingReport';
    import CustomerVisitApi from '../../api/customerVisit';
    import axios from 'axios'
    import qs from 'qs'

    export default {
        name: 'basetable',
        data() {
            return {
                tableData: [],
                page: {pageNo: 1, pageSize: 20},
                multipleSelection: [],
                is_search: false,
                editVisible: false,
                delVisible: false,
                form: {
                    name: '',
                    account: '',
                    birthday: '',
                    email: '',
                    phone: '',
                    deptId: '',
                    roleIds: []
                },
                idx: -1,
                ids: [],
                req: {},
                url: '',
                reqExport: {
                    startTime: '',
                    endTime: ''
                },
                accountInput: true,
                loading: false,
                deptTreeData: [],
                defaultProps: {
                    children: 'children',
                    label: 'name'
                },
                customerList: [],
                isShow: false,
            }

        },
        created() {
            this.getData();
        },
        computed: {},
        methods: {
            selectTrigger(type) {
                if (type != 4) {
                    this.isShow = false;
                } else {
                    this.isShow = true;
                }
            },
            filterTag(value, row) {
                return row.visitType === value;
            },
            getRoleList() {
                CustomerVisitApi.getClinetlist().then((res) => {
                    if (res.error === false) {
                        this.customerList = res.data;

                    } else {
                        this.$message.error(res.msg);
                    }
                }, (err) => {
                    this.$message.error(err.msg);
                });
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
            reload() {
                this.page.pageNo = 1
                this.getData()
            },
            // 获取 easy-mock 的模拟数据
            getData() {
                this.loading = true;
                this.req.currentPage = this.page.pageNo
                this.req.pageSize = this.page.pageSize
                SigningReportApi.getData(this.req).then((res) => {
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
            search() {
                this.is_search = true;
                this.getData();
            },
            handleExport() {
                this.url = "http://localhost:8084/beam_ht/exportdata/excel"
                this.url = "http://localhost:8084/beam_ht/exportdata/excel";
                if (this.reqExport.startTime !== null && this.reqExport.startTime !== '' && this.reqExport.endTime !== null && this.reqExport.endTime !== '') {
                    this.url = this.url + "?startTime=" + this.reqExport.startTime + "&endTime=" + this.reqExport.endTime;
                }
                window.open(this.url);

            },
        }
    }

</script>

<style scoped>
    .handle-box {
        margin-bottom: 20px;
    }

    .handle-select {
        width: 120px;
    }

    .handle-input {
        width: 300px;
        display: inline-block;
    }

    .del-dialog-cnt {
        font-size: 16px;
        text-align: center
    }

    .table {
        width: 100%;
        font-size: 14px;
    }

    .red {
        color: #ff0000;
    }
</style>

