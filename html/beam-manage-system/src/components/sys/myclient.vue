<template>
    <div class="table">
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item><i class="el-icon-lx-people"></i> 我的客户</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="container">
            <div class="handle-box">


                <el-input style="width: 150px" v-model="req.customerStoreName" placeholder="客户店名"></el-input>
                <el-input style="width: 150px" v-model="req.customerPhone" placeholder="联系人电话号码"></el-input>
                <el-select style="width: 100px" v-model="req.customerType" placeholder="客户类型" clearable="true">
                    <el-option
                        v-for="item in customerType"
                        :key="item.value"
                        :label="item.name"
                        :value="item.value">
                    </el-option>
                </el-select>
                <el-button type="primary" icon="search" @click="search">搜索</el-button>
                <el-button type="primary" icon="add" class="handle-del mr10" @click="handleAdd">添加我的客户</el-button>
            </div>

            <el-table row-key="id" :data="tableData" v-loading="loading" border class="table" ref="multipleTable"
                      @selection-change="handleSelectionChange">
                <el-table-column type="selection" width="55" align="center"></el-table-column>
                <el-table-column label="客户ID" align="center" prop="id" v-if="false">
                </el-table-column>
                <el-table-column label="客户编码" align="center" prop="customerId">
                </el-table-column>
                <el-table-column label="客户店名" align="center" prop="customerStoreName">
                </el-table-column>
                <el-table-column label="联系人姓名" align="center" prop="customerContactName">
                </el-table-column>
                <el-table-column label="联系人电话" align="center" prop="customerPhone">
                </el-table-column>
                <el-table-column label="添加人" align="center" prop="createPersonName">
                </el-table-column>
                <el-table-column label="添加时间" align="center" prop="createTime">
                </el-table-column>
                <el-table-column label="销售人" align="center" prop="salesName">
                </el-table-column>
                <!-- <el-table-column label="修改时间" align="center" prop="updateTime">-->
                <!--  </el-table-column>-->
                <el-table-column label="客户状态" align="center" prop="customerTag"
                                 :filters="[{ text: '未知', value: 1 },{ text: '拜访', value: 2 },{ text: '意向', value: 3 }, { text: '签约', value: 4 }]"
                                 :filter-method="filterTag"
                                 filter-placement="bottom-end">
                    <template slot-scope="scope">
                        <el-tag
                            :type="scope.row.customerTag === 1 ? 'primary' : 'success'"
                            disable-transitions> {{changeTagLength(scope.row.customerTag)}}
                        </el-tag>
                    </template>

                </el-table-column>
                <el-table-column label="客户类型" align="center" prop="customerType">
                    <template slot-scope="scope">
                        <span v-if="scope.row.customerType == '2'"><font class="red">{{changeRemarkLength(scope.row.customerType)}}</font></span>
                        <span v-else>{{changeRemarkLength(scope.row.customerType)}}</span>
                    </template>
                </el-table-column>
                <el-table-column label="操作" width="180" align="center">
                    <template slot-scope="scope">
                        <el-button type="text" icon="el-icon-headset" @click="handleVisit(scope.$index, scope.row)">拜访
                        </el-button>
                        <el-button type="text" icon="el-icon-view" class="green"
                                   @click="handleVisitInfo(scope.$index, scope.row)">拜访记录
                        </el-button>
                        <el-button type="text" icon="el-icon-delete" class="red"
                                   @click="handleDelete(scope.$index, scope.row)">释放
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

        </div>
        <!-- 编辑弹出框 -->
        <el-dialog title="客户信息" :visible.sync="editVisible" width="60%">
            <div class="handle-box">
                <el-input style="width: 150px" v-model="myReq.customerStoreName" placeholder="客户店名"></el-input>
                <el-input style="width: 150px" v-model="myReq.customerPhone" placeholder="联系人电话号码"></el-input>
                <el-select style="width: 100px" v-model="myReq.customerType" placeholder="客户类型">
                    <el-option
                        v-for="item in customerType"
                        :key="item.value"
                        :label="item.name"
                        :value="item.value">
                    </el-option>
                </el-select>
                <el-button type="primary" icon="search" @click="mySearch">搜索</el-button>
                <el-button type="primary" icon="search" @click="addMyClient">导入</el-button>
            </div>
            <el-table :data="tableClinetData" v-loading="myLoading" border class="table" ref="multipleTable"
                      @selection-change="handleMySelectionChange">
                <el-table-column type="selection" width="55" align="center"></el-table-column>
                <el-table-column label="客户店名" align="center" prop="customerStoreName"></el-table-column>
                <el-table-column label="联系人姓名" align="center" prop="customerContactName"></el-table-column>
                <el-table-column label="联系人电话" align="center" prop="customerPhone"></el-table-column>
                <el-table-column label="客户类型" align="center" prop="customerType">
                    <template slot-scope="scope">
                        <span v-if="scope.row.customerType == '2'"><font class="red">{{changeRemarkLength(scope.row.customerType)}}</font></span>
                        <span v-else>{{changeRemarkLength(scope.row.customerType)}}</span>
                    </template>
                </el-table-column>
            </el-table>
            <div class="mypagination">
                <el-pagination
                    background
                    :page-sizes="[10, 20, 30, 40, 50]"
                    :page-size="myPage.pageSize"
                    :current-page="myPage.pageNo"
                    @current-change="handleCurrentMyChange"
                    @size-change="changeMyPageSize"
                    layout="prev, pager, next"
                    :total="myPage.totalRows">
                </el-pagination>
            </div>
        </el-dialog>
        <!--添加拜访记录-->
        <el-dialog title="添加拜访" :visible.sync="editVisitVisible" width="30%">
            <el-form ref="form" :model="form" :rules="rules" label-width="100px">
                <el-input id="customerId" v-model="form.customerId" v-show="false">
                </el-input>
                <el-form-item label="拜访对象" prop="customerStoreName">
                    <el-input v-model="form.customerStoreName" disabled="true"></el-input>
                </el-form-item>
                <el-form-item label="拜访时间" prop="visitTime">
                    <el-date-picker type="date" placeholder="选择日期" v-model="form.visitTime">
                    </el-date-picker>
                </el-form-item>
                <el-form-item label="拜访进度" prop="visitSchedule">
                    <el-input type="textarea" v-model="form.visitSchedule"></el-input>
                </el-form-item>
                <el-form-item label="拜访内容" prop="visitInfo">
                    <el-input type="textarea" v-model="form.visitInfo"></el-input>
                </el-form-item>
                <el-form-item label="拜访结果" prop="resultInfo">
                    <el-input type="textarea" v-model="form.resultInfo"></el-input>
                </el-form-item>
                <el-form-item label="拜访标签">
                    <el-select v-model="form.visitType" placeholder="选择标签" @change="selectTrigger(form.visitType)"
                               clearable="true">
                        <el-option
                            v-for="item in typeList"
                            :key="item.id"
                            :label="item.typeName"
                            :value="item.id">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item v-show="isShow" label="签约金额">
                    <el-input v-model="form.amount" style="width: 210px"></el-input>
                    元
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="editVisitVisible = false">取 消</el-button>
                <el-button type="primary" :loading="loading" @click="saveVisitEdit('form')">确 定</el-button>
            </span>
        </el-dialog>
        <!-- 编辑弹出框 -->
        <el-dialog title="查看记录" :visible.sync="viewVisible" width="70%">
            <el-input v-model="viewReq.customerId" placeholder="customerId" v-show="false"></el-input>
            <el-table :data="tableViewData" v-loading="viewLoading" border class="table" ref="multipleTable"
                      @selection-change="handleSelectionChange">
                <el-table-column type="selection" width="55" align="center"></el-table-column>
                <el-table-column label="客户ID" align="center" prop="id" v-if="false">
                </el-table-column>
                <el-table-column label="客户店名" align="center" prop="customerStoreName">
                </el-table-column>
                <el-table-column prop="visitTime" align="center" label="拜访时间" :formatter="dateFormat">
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
                            disable-transitions> {{changeViewRemarkLength(scope.row.visitType)}}
                        </el-tag>
                    </template>

                </el-table-column>
                <el-table-column label="签约金额" align="center" prop="amount">
                </el-table-column>
            </el-table>
            <div class="pagination">
                <el-pagination
                    background
                    :page-sizes="[10, 20, 30, 40, 50]"
                    :page-size="viewPage.pageSize"
                    :current-page="viewPage.pageNo"
                    @current-change="handleViewCurrentChange"
                    @size-change="changeViewPageSize"
                    layout="prev, pager, next"
                    :total="viewPage.totalRows">
                </el-pagination>
            </div>
            <span slot="footer" class="dialog-footer">
                <el-button @click="viewVisible = false">返回</el-button>
            </span>
        </el-dialog>
        <!-- 删除提示框 -->
        <el-dialog title="提示" :visible.sync="delVisible" width="300px" center>
            <div class="del-dialog-cnt">客户释放后将回归客户池中，是否确定释放客户？</div>
            <span slot="footer" class="dialog-footer">
                <el-button @click="delVisible = false">取 消</el-button>
                <el-button type="primary" @click="deleteRow">确 定</el-button>
            </span>
        </el-dialog>
    </div>
</template>

<script>
    import MyClientApi from '../../api/myclient';
    import ElSelectDropdown from "element-ui/packages/select/src/select-dropdown";

    export default {
        components: {ElSelectDropdown},
        name: 'basetable',
        data() {
            return {
                configMenuDialog: false,
                tableData: [],
                tableViewData: [],
                tableClinetData: [],
                page: {pageNo: 1, pageSize: 10},
                myPage: {pageNo: 1, pageSize: 10},
                viewPage: {pageNo: 1, pageSize: 10},
                multipleSelection: [],
                multipleMySelection: [],
                is_search: false,
                is_mySearch: false,
                editVisible: false,
                editVisitVisible: false,
                viewVisible: false,
                delVisible: false,
                addVisible: false,
                clientele: {},
                customerList: [],
                idx: -1,
                ids: [],
                addIds: [],
                req: {},
                myReq: {},
                viewReq: {},
                form: {
                    customerId: '',
                    visitSchedule: '',
                    customerStoreName: '',
                    visitTime: '',
                    visitInfo: '',
                    resultInfo: '',
                    email: '',
                    phone: '',
                    deptId: '',
                    roleIds: []
                },
                rules: {
                    customerStoreName: [
                        {required: true, message: '请输入拜访对象', trigger: 'blur'},
                    ],
                    visitTime: [
                        {type: 'date', required: true, message: '请选择日期', trigger: 'change'}
                    ],
                    visitSchedule: [
                        {required: true, message: '请输入拜访进度', trigger: 'blur'},
                    ],
                    visitInfo: [
                        {required: true, message: '请输入拜访内容', trigger: 'blur'},
                    ],
                    resultInfo: [
                        {required: true, message: '请输入拜访结果', trigger: 'blur'},
                    ]
                },
                accountInput: true,
                loading: false,
                myLoading: false,
                viewLoading: false,
                customerType: [
                    {value: 1, name: "普通客户"},
                    {value: 2, name: "重点客户"}
                ],
                menuTreeData: [],
                defaultProps: {
                    children: 'children',
                    label: 'name'
                },
                checkMenuData: [],
                itemlist: [],
                placeholder: "搜索",
                nodatatext: "暂无数据",
                selectValue: "",
                roleId: null,
                typeList: [
                    {id: 2, typeName: "其他"},
                    {id: 3, typeName: "意向"},
                    {id: 4, typeName: "签约"}],
                isShow: false,
            }
        },
        created() {
            this.getData();
        },
        myCreated() {
            this.getMyData();
        },
        computed: {
            changeViewRemarkLength() {
                return function (text) {
                    if (text == "2") {
                        return '其他'
                    } else if (text == "3") {
                        return '意向'
                    } else if (text == "4") {
                        return '签约'
                    }
                }
            },
            changeRemarkLength() {
                return function (text) {
                    if (text == "1") {
                        return '普通客户'
                    } else if (text == "2") {
                        return '重点客户'
                    }
                }
            },
            changeTagLength() {
                return function (text) {
                    if (text == "1") {
                        return '未知'
                    } else if (text == "2") {
                        return '拜访'
                    } else if (text == "3") {
                        return '意向'
                    } else if (text == "4") {
                        return '签约'
                    } else if (text == "5") {
                        return '完成'
                    }
                }
            },
        },
        methods: {
            dateFormat: function (row, column) {
                var date = row[column.property];
                if (date != null) {
                    const timeFormat = date.substring(0, 10);
                    return timeFormat;
                }
            },
            selectTrigger(type) {
                if (type != 4) {
                    this.isShow = false;
                } else {
                    this.isShow = true;
                }
            },
            filterTag(value, row) {
                return row.customerTag === value;
            },
            itemClick(data) {
                this.selectValue = data
            },
            getInputValue(searchvalue) {
                console.log(searchvalue)
                // 请求获取筛选列表

            },
            handleCurrentChange(val) {
                this.page.pageNo = val;
                this.getData();
            },
            handleViewCurrentChange(val) {
                this.viewPage.pageNo = val;
                this.getViewData();
            },
            changePageSize(value) { // 修改每页条数size
                this.viewPage.pageNo = 1
                this.viewPage.pageSize = value
                this.tableData = null
                this.getData()
            },
            changeViewPageSize(value) { // 修改每页条数size
                this.page.pageNo = 1
                this.page.pageSize = value
                this.tableViewData = null
                this.getViewData()
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
                MyClientApi.getData(this.req).then((res) => {
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
            getViewData() {
                this.viewLoading = true;
                this.viewReq.currentPage = this.viewPage.pageNo;
                this.viewReq.pageSize = this.viewPage.pageSize;
                MyClientApi.getViewData(this.viewReq).then((res) => {
                    this.viewLoading = false;
                    if (res.error === false) {
                        this.tableViewData = res.data.records ? res.data.records : []
                        this.viewPage.pageNo = parseInt(res.data.current)
                        this.viewPage.totalRows = parseInt(res.data.total)
                        this.tableViewData.forEach(item => {
                            item.status = Boolean(item.status)
                        })
                    } else {
                        this.$message.error(res.msg);
                    }
                }, (err) => {
                    this.viewLoading = false;
                    this.$message.error(err.msg);
                });
            },
            search() {
                this.is_search = true;
                this.getData();
            },
            saveEdit() {
                this.loading = true
                MyClientApi.save(this.clientele).then((res) => {
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
            saveVisitEdit(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        this.loading = true
                        MyClientApi.saveVisitEdit(this.form).then((res) => {
                            this.loading = false
                            if (res.error === false) {
                                this.editVisitVisible = false
                                this.$message.success(res.msg);
                                this.reload()
                            } else {
                                this.$message.error(res.msg);
                            }
                        }, (err) => {
                            this.loading = false
                            this.$message.error(err.msg);
                        })
                    }
                })

            },
            deleteRow() {
                MyClientApi.deleteMyClient(this.ids).then((res) => {
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
            handleAdd() {
                this.clientele = {};
                this.editVisible = true;
                this.getMyData();
            },
            handleDelete(index, row) {
                this.ids = [row.id];
                this.delVisible = true;
            },
            handleVisit(index, row) {
                this.isShow = false;
                this.form = {
                    customerId: row.customerId,
                    customerStoreName: row.customerStoreName
                };
                this.editVisitVisible = true;
            },
            handleVisitInfo(index, row) {
                this.viewReq = {
                    customerId: row.customerId
                };
                this.viewVisible = true;
                this.getViewData();

            },
            handleEdit(index, row) {
                this.idx = index;
                const item = this.tableData[index];
                this.clientele = item;
                this.editVisible = true;

            },
            //
            handleCurrentMyChange(val) {
                this.myPage.pageNo = val;
                this.getMyData();
            },
            changeMyPageSize(value) { // 修改每页条数size
                this.myPage.pageNo = 1
                this.myPage.pageSize = value
                this.tableClinetData = null
                this.getMyData()
            },
            getMyData() {
                this.myLoading = true;
                this.myReq.currentPage = this.myPage.pageNo
                this.myReq.pageSize = this.myPage.pageSize
                MyClientApi.getMyData(this.myReq).then((res) => {
                    this.myLoading = false;
                    if (res.error === false) {
                        this.tableClinetData = res.data.records ? res.data.records : []
                        this.myPage.pageNo = parseInt(res.data.current)
                        this.myPage.totalRows = parseInt(res.data.total)
                        this.tableClinetData.forEach(item => {
                            item.status = Boolean(item.status)
                        })
                    } else {
                        this.$message.error(res.msg);
                    }
                }, (err) => {
                    this.tableClinetData = false;
                    this.$message.error(err.msg);
                });
            },
            myReload() {
                this.myPage.pageNo = 1
                this.getMyData()
            },
            mySearch() {
                this.is_mySearch = true;
                this.getMyData();
            },
            addMyClient() {
                this.addVisible = true;
                this.addIds = [];
                const length = this.multipleMySelection.length;
                for (let i = 0; i < length; i++) {
                    this.addIds.push(this.multipleMySelection[i].id);
                }
                MyClientApi.addMyClient(this.addIds).then((res) => {
                    if (res.error === false) {
                        this.$message.success(res.msg);
                        this.myReload();
                        this.reload()
                    } else {
                        this.$message.error(res.msg);
                    }

                }, (err) => {
                    this.$message.error(err.msg);
                })
                this.delVisible = false;
            },
            handleMySelectionChange(val) {
                this.multipleMySelection = val;
            },

        },

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

    .green {
        color: #44BB5C;
    }
</style>
