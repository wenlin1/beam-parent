<template>
    <div class="table">
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item><i class="el-icon-lx-people"></i> 客户信息</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="container">
            <div class="handle-box">


                <el-input style="width: 150px" v-model="req.customerStoreName" placeholder="客户店名"></el-input>
                <el-input style="width: 150px" v-model="req.customerPhone" placeholder="联系人电话号码"></el-input>
                <el-input style="width: 150px" v-model="req.salesName" placeholder="销售人"></el-input>
                <el-select style="width: 100px" v-model="req.customerType" placeholder="客户类型" clearable="true">
                    <el-option
                        v-for="item in customerTypeList"
                        :key="item.id"
                        :label="item.name"
                        :value="item.id">
                    </el-option>
                </el-select>
                <el-button type="primary" icon="search" @click="search">搜索</el-button>
                <el-button type="primary" icon="add" class="handle-del mr10" @click="handleAdd">新增</el-button>
            </div>

            <el-table row-key="id" :data="tableData" v-loading="loading" border class="table" ref="multipleTable"
                      @selection-change="handleSelectionChange">
                <el-table-column type="selection" width="55" align="center"></el-table-column>
                <el-table-column label="客户ID" align="center" prop="id" v-if="show">
                </el-table-column>
                <el-table-column label="客户编码" align="center" prop="customerId">
                </el-table-column>
                <el-table-column label="客户店名" align="center" prop="customerStoreName">
                </el-table-column>
                <el-table-column label="联系人姓名" align="center" prop="customerContactName">
                </el-table-column>
                <el-table-column label="联系人电话" align="center" prop="customerPhone">
                </el-table-column>
                <el-table-column label="添加人/添加时间" align="center" prop="createPersonName" show-overflow-tooltip="true">
                </el-table-column>
                <el-table-column label="销售人" align="center" prop="salesName">
                </el-table-column>
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
                        <el-button type="text" icon="el-icon-edit" @click="handleEdit(scope.$index, scope.row)">编辑
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

        </div>
        <!-- 编辑弹出框 -->
        <el-dialog title="编辑" :visible.sync="editVisible" width="30%">
            <el-form ref="clientele" :model="clientele" :rules="rules" label-width="100px">
                <el-form-item label="客户店名" prop="customerStoreName">
                    <el-input v-model="clientele.customerStoreName"></el-input>
                </el-form-item>
                <el-form-item label="联系人姓名" prop="customerContactName">
                    <el-input v-model="clientele.customerContactName"></el-input>
                </el-form-item>
                <el-form-item label="联系人电话" prop="customerPhone">
                    <el-input style="width: 200px" v-model="clientele.customerPhone"></el-input>
                </el-form-item>
                <el-form-item label="绑定销售人" prop="salesAccount">
                    <el-select v-model="clientele.salesAccount" placeholder="选择销售人" clearable="ture" filterable="true">
                        <el-option
                            v-for="item in salesList"
                            :key="item.id"
                            :label="item.name"
                            :value="item.id">
                        </el-option>
                    </el-select>
                </el-form-item>

                <el-form-item label="客户类型" prop="customerType">
                    <el-select v-model="clientele.customerType" placeholder="请选择">
                        <el-option
                            v-for="item in customerTypeList"
                            :key="item.id"
                            :label="item.name"
                            :value="item.id">
                        </el-option>
                    </el-select>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="editVisible = false">取 消</el-button>
                <el-button type="primary" :loading="loading" @click="saveEdit('clientele')">确 定</el-button>
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

<script>
    import ClienteleApi from '../../api/clientele';
    import ElSelectDropdown from "element-ui/packages/select/src/select-dropdown";
    /*import Dropdown from '@/components/common/dropdown.vue';*/

    export default {
        components: {ElSelectDropdown},
        name: 'basetable',
        data() {

            var validateMobilePhone = (rule, value, callback) => {
                if (value === '') {
                    callback(new Error('手机号不可为空'));
                } else {
                    if (value !== '') {
                        var reg = /^1[3456789]\d{9}$/;
                        if (!reg.test(value)) {
                            callback(new Error('请输入有效的手机号码'));
                        }
                    }
                    callback();
                }
            };

            return {
                configMenuDialog: false,
                tableData: [],
                page: {pageNo: 1, pageSize: 10},
                multipleSelection: [],
                is_search: false,
                editVisible: false,
                delVisible: false,
                clientele: {
                    customerStoreName: '',
                    customerContactName: '',
                    customerPhone: '',
                    customerType: ''
                },
                rules: {
                    customerStoreName: [
                        {required: true, message: '请输入客户店名', trigger: 'blur'},
                    ],
                    customerContactName: [
                        {required: true, message: '请输入联系人姓名', trigger: 'blur'}
                    ],
                    customerPhone: [
                        {required: true, message: '请输入联系人电话', trigger: 'blur'},
                        {min: 11, max: 11, message: '11位手机号', trigger: 'blur'},
                        {validator: validateMobilePhone, trigger: 'blur'},
                    ],
                    customerType: [
                        {required: true, message: '请选择客户类型', trigger: 'change'}
                    ],
                },
                idx: -1,
                ids: [],
                req: {},
                accountInput: true,
                loading: false,
                menuTreeData: [],
                defaultProps: {
                    children: 'children',
                    label: 'name'
                },
                checkMenuData: [],
                roleId: null,
                customerTypeList: [
                    {id: 1, name: "普通客户"},
                    {id: 2, name: "重点客户"}
                ],
                salesList: [],
                showId: false
            }
        },
        created() {
            this.getData();
        },
        computed: {
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
            filterTag(value, row) {
                return row.customerTag === value;
            },
            getUserList() {
                ClienteleApi.getUser().then((res) => {
                    if (res.error === false) {
                        this.salesList = res.data;

                    } else {
                        this.$message.error(res.msg);
                    }
                }, (err) => {
                    this.$message.error(err.msg);
                });
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
                ClienteleApi.getData(this.req).then((res) => {
                    this.loading = false;
                    if (res.error === false) {
                        this.tableData = res.data.records ? res.data.records : []
                        this.page.pageNo = parseInt(res.data.current)
                        this.page.totalRows = parseInt(res.data.total)
                        this.tableData.forEach(item => {
                            item.status = Boolean(item.status)
                            item["createPersonName"] = item["createPersonName"] + "/" + item["createTime"];
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
            saveEdit(clientele) {
                this.$refs[clientele].validate((valid) => {
                    if (valid) {
                        this.loading = true
                        ClienteleApi.save(this.clientele).then((res) => {
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
                    }
                });
            },
            deleteRow() {
                ClienteleApi.batchDelete(this.ids).then((res) => {
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
                this.getUserList();
                this.editVisible = true;
            },
            handleDelete(index, row) {
                this.ids = [row.id];
                this.delVisible = true;
            },
            handleEdit(index, row) {
                this.idx = index;
                const item = this.tableData[index];
                this.clientele = item;
                this.editVisible = true;

            },
            //计算属性

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

</style>
