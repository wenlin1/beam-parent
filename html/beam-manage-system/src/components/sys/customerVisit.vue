<template>
    <div class="table">
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item><i class="el-icon-lx-people"></i> 客户拜访</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="container">

            <el-container>
                <el-container>
                    <el-header>
                        <el-input style="width: 120px" v-model="req.customerStoreName" placeholder="客户名称"></el-input>
                        <el-input style="width: 120px" v-model="req.visitType" placeholder="标签"></el-input>
                        <el-button type="primary" icon="search" @click="search">搜索</el-button>
                        <el-button type="primary" icon="add" class="handle-del mr10" @click="handleAdd">新增拜访记录</el-button>
                    </el-header>
                    <el-main>
                        <el-table :data="tableData" v-loading="loading" border class="table" ref="multipleTable"
                                  @selection-change="handleSelectionChange">
                            <el-table-column type="selection" width="55" align="center"></el-table-column>
                            <el-table-column label="客户ID" align="center" prop="id" v-if="false">
                            </el-table-column>
                            <el-table-column label="拜访对象" align="center" prop="customerStoreName">
                            </el-table-column>
                            <el-table-column label="拜访进度" align="center" prop="visitSchedule">
                            </el-table-column>
                            <el-table-column label="拜访内容" align="center" prop="visitInfo">
                            </el-table-column>
                            <el-table-column label="拜访结果" align="center" prop="resultInfo">
                            </el-table-column>
                            <el-table-column label="拜访标签" align="center" prop="visitType"
                                             :filters="[{ text: '意向', value: 3 }, { text: '签约', value: 4 },{ text: '其他', value: 2}]"
                                             :filter-method="filterTag"
                                             filter-placement="bottom-end">
                                <template slot-scope="scope">
                                    <el-tag
                                        :type="scope.row.visitType === '意向' ? 'primary' : 'success'"
                                        disable-transitions>  {{changeRemarkLength(scope.row.visitType)}}</el-tag>
                                </template>

                            </el-table-column>
                            <el-table-column label="签约金额" align="center" prop="amount">
                            </el-table-column>
                            <el-table-column label="操作" width="180" align="center">
                                <template slot-scope="scope">
                                    <el-button type="text" icon="el-icon-edit"
                                               @click="handleEdit(scope.$index, scope.row)">编辑
                                    </el-button>
                                    <el-button type="text" icon="el-icon-delete" class="red"
                                               @click="handleDelete(scope.$index, scope.row)">删除
                                    </el-button>
                                </template>
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

        <!-- 编辑弹出框 -->
        <el-dialog title="编辑" :visible.sync="editVisible" width="50%">
            <el-form ref="form" :model="form" label-width="100px">
                <el-form-item label="拜访对象">
                    <el-select v-model="form.customerId"  placeholder="选择客户" clearable="ture" filterable="true">
                        <el-option
                            v-for="item in customerList"
                            :key="item.id"
                            :label="item.customerStoreName"
                            :value="item.id">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="拜访进度">
                    <el-input v-model="form.visitSchedule"></el-input>
                </el-form-item>
                <el-form-item  label="拜访内容">
                    <el-input  v-model="form.visitInfo"></el-input>
                </el-form-item>
                <el-form-item label="拜访结果">
                    <el-input v-model="form.resultInfo"></el-input>
                </el-form-item>
                <el-form-item  label="拜访标签">
                    <el-select v-model="form.visitType"  placeholder="选择标签"  @change="selectTrigger(form.visitType)"clearable="true">
                        <el-option
                            v-for="item in typeList"
                            :key="item.id"
                            :label="item.typeName"
                            :value="item.id">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item  v-show="isShow" label="签约金额">
                    <el-input v-model="form.amount"></el-input>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="editVisible = false">取 消</el-button>
                <el-button type="primary" :loading="loading" @click="saveEdit">确 定</el-button>
            </span>
        </el-dialog>

        <!-- 删除提示框 -->
        <el-dialog title="提示" :visible.sync="delVisible" width="300px" center>
            <div class="del-dialog-cnt">删除不可恢复，是否确定删除？</div>
            <span slot="footer" class="dialog-footer">
                <el-button @click="delVisible = false">取 消</el-button>
                <el-button type="primary" @click="deleteRow">确 定</el-button>
            </span>
        </el-dialog>
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
    import CustomerVisitApi from '../../api/customerVisit';

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
                    deptId:'',
                    roleIds:[]
                },
                idx: -1,
                ids: [],
                req: {},
                accountInput: true,
                loading: false,
                deptTreeData: [],
                defaultProps: {
                    children: 'children',
                    label: 'name'
                },
                customerList:[],
                typeList:[
                    {id:2,typeName:"其他"},
                    {id:3,typeName:"意向"},
                    {id:4,typeName:"签约"}],
                isShow:false,
            }

        },
        created() {
            this.getData();
        },
        computed: {
            changeRemarkLength() {
                return function (text) {
                    if (text =="2") {
                        return '其他'
                    }else if (text =="3") {
                        return '意向'
                    }else if (text =="4") {
                      return '签约'
                  }
                }
            },
        },
        methods: {
            selectTrigger(type){
                if(type != 4){
                    this.isShow=false;
                }else{
                    this.isShow=true;
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
                CustomerVisitApi.getData(this.req).then((res) => {
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

            handleAdd() {
                this.form = {};
                this.getRoleList()
                this.editVisible = true;
                this.accountInput = false;
                this.form.status = true;
            },
            handleEdit(index, row) {
                this.idx = index;
                CustomerVisitApi.info({id:row.id}).then((res) => {
                    this.loading = false;
                    if (res.error === false) {
                        this.form = res.data;
                        this.form.status = Boolean(this.form.status)
                    } else {
                        this.$message.error(res.msg);
                    }
                }, (err) => {
                    this.loading = false;
                    this.$message.error(err.msg);
                });
                this.getRoleList();
                this.editVisible = true;
                this.accountInput = true;
            },
            handleSelectionChange(val) {
                this.multipleSelection = val;
            },
            // 保存编辑
            saveEdit() {
                this.loading = true
                CustomerVisitApi.save(this.form).then((res) => {
                    this.loading = false
                    if (res.error === false) {
                        this.editVisible = false
                        this.$message.success(res.msg);
                        this.reload()
                    } else {
                        this.$message.error(res.msg);
                    }
                }, (err) => {
                    this.loading = false
                    this.$message.error(err.msg);
                })

            },
            // 确定删除
            deleteRow() {
                SysUserApi.batchDelete(this.ids).then((res) => {
                    if (res.error === false) {
                        this.$message.success(res.msg);
                        this.reload()
                    } else {
                        this.$message.error(res.msg);
                    }

                }, (err) => {
                    this.$message.error(err.msg);
                })
                this.delVisible = false;
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
