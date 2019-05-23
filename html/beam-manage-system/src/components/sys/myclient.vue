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
                <el-button type="primary" icon="search" @click="search">搜索</el-button>
                <el-button type="primary" icon="add" class="handle-del mr10" @click="handleAdd">新增</el-button>
            </div>

            <el-table row-key="id" :data="tableData" v-loading = "loading" border class="table" ref="multipleTable" @selection-change="handleSelectionChange">
                <el-table-column type="selection" width="55" align="center"></el-table-column>
                <el-table-column label="客户ID" align="center" prop="id">
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
                <el-table-column label="客户类型" align="center" prop="customerType">
                    <template slot-scope="scope">
                        <span v-if="scope.row.customerType == '2'"><font class="red">{{changeRemarkLength(scope.row.customerType)}}</font></span>
                        <span v-else>{{changeRemarkLength(scope.row.customerType)}}</span>
                    </template>
                </el-table-column>
                <el-table-column label="操作" width="180" align="center">
                    <template slot-scope="scope">
                        <el-button type="text" icon="el-icon-edit" @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
                        <el-button type="text" icon="el-icon-delete" class="red" @click="handleDelete(scope.$index, scope.row)">删除</el-button>

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
            <el-form ref="clientele" :model="clientele" label-width="100px">
                <el-form-item label="客户店名"  prop="customerStoreName">
                    <el-input  v-model="clientele.customerStoreName"></el-input>
                </el-form-item>
                <el-form-item label="联系人姓名" prop="customerContactName">
                    <el-input v-model="clientele.customerContactName"></el-input>
                </el-form-item>
                <el-form-item  label="联系人电话" prop="customerPhone">
                    <el-input v-model="clientele.customerPhone"></el-input>
                </el-form-item>
                <el-form-item  label="销售人" prop="salesName">
                    <!--  <dropdown :item-click="dropDownClick" :isNeedSearch="true" :itemlist="itemlist"></dropdown>-->
                    <!-- <el-input v-model="clientele.salesName"><dropdown :itemlist="itemlist" :placeholder="placeholder"
                                                                       :nodatatext="nodatatext"></dropdown></el-input>-->


                </el-form-item>
                <el-form-item label="客户类型" prop="customerType">
                    <el-select v-model="clientele.customerType" placeholder="请选择">
                        <el-option
                            v-for="item in customerType"
                            :key="item.value"
                            :label="item.name"
                            :value="item.value">
                        </el-option>
                    </el-select>
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

<script>
    import MyClientApi from '../../api/myclient';
    import ElSelectDropdown from "element-ui/packages/select/src/select-dropdown";

    export default {
        components: {ElSelectDropdown},
        name: 'basetable',
        data() {
            return {
                configMenuDialog:false,
                tableData: [],
                page: {pageNo: 1, pageSize: 10},
                multipleSelection: [],
                is_search: false,
                editVisible: false,
                delVisible: false,
                clientele: {},
                idx: -1,
                ids: [],
                req: {},
                accountInput: true,
                loading: false,
                customerType:[
                    {value:1,name:"普通客户"},
                    {value:2,name:"重点客户"}
                ],
                menuTreeData: [],
                defaultProps: {
                    children: 'children',
                    label: 'name'
                },
                checkMenuData:[],
                itemlist: [],
                placeholder: "搜索",
                nodatatext: "暂无数据",
                selectValue: "",
                roleId:null
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
                    } else if (text =="2") {
                        return '重点客户'
                    }
                }
            },
        },
        methods: {
            itemClick(data) {
                this.selectValue= data
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
            deleteRow() {
                MyClientApi.batchDelete(this.ids).then((res) => {
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
            },
            handleDelete(index, row) {
                this.ids = [row.id];
                this.delVisible = true;
            },
            handleEdit(index, row) {
                this.idx = index;
                const item = this.tableData[index];
                this.clientele = item;
                this.editVisible=true;

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
