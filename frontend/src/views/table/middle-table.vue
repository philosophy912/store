<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input v-model="listQuery.name" clearable placeholder="请输入分类名字" style="width: 200px;" class="filter-item" @keyup.enter.native="handleFilter" @clear="clearName" />
      <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">查找</el-button>
      <el-button class="filter-item" style="margin-left: 10px;" type="success" icon="el-icon-edit" @click="handleCreate">添加</el-button>
    </div>

    <el-table :key="tableKey" v-loading="listLoading" :data="list" border fit highlight-current-row style="width: 100%;" @sort-change="sortChange">
      <el-table-column fixed type="expand">
        <template slot-scope="scope">
          <el-table :data="scope.row.formulaVos" border style="width: 100%">
            <el-table-column prop="type" label="类型" align="center" />
            <el-table-column prop="name" label="名称" align="center" />
            <el-table-column prop="count" label="数量" align="center" />
          </el-table>
        </template>
      </el-table-column>
      <el-table-column label="序号" prop="id" sortable="custom" align="center" width="80" :class-name="getSortClass('id')">
        <template slot-scope="{row}">
          <span>{{ row.id }}</span>
        </template>
      </el-table-column>
      <el-table-column label="名称" min-width="150px" align="center">
        <template slot-scope="{row}">
          <span class="link-type" @click="handleUpdate(row)">{{ row.name }}</span>
        </template>
      </el-table-column>
      <el-table-column label="容量" min-width="150px" align="center">
        <template slot-scope="{row}">
          <span class="link-type" @click="handleUpdate(row)">{{ row.capacity }}</span>
        </template>
      </el-table-column>
      <el-table-column label="单位" min-width="150px" align="center">
        <template slot-scope="{row}">
          <span class="link-type" @click="handleUpdate(row)">{{ row.unit }}</span>
        </template>
      </el-table-column>
      <el-table-column label="价格" min-width="150px" align="center">
        <template slot-scope="{row}">
          <span class="link-type" @click="handleUpdate(row)">{{ parseNumber(row.price) }} 元</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="230" class-name="small-padding fixed-width">
        <template slot-scope="{row,$index}">
          <el-button type="primary" size="mini" @click="handleUpdate(row)">编辑</el-button>
          <el-button v-if="row.status!=='deleted'" size="mini" type="danger" @click="handleDelete(row,$index)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getList" />

    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form ref="dataForm" :rules="rules" :model="temp" label-position="left" label-width="100px" style="width: 600px; margin-left:50px;">
        <el-form-item label="名称" prop="name">
          <el-input v-model="temp.name" />
        </el-form-item>
        <el-form-item label="容量" prop="capacity">
          <el-input v-model="temp.capacity" />
        </el-form-item>
        <el-form-item label="单位" prop="unit">
          <el-input v-model="temp.unit" />
        </el-form-item>
        <el-form-item v-for="(formula, index) in temp.formulaVos" :key="index" :label="formula.type" label-width="80px">
          <el-col :span="6">
            <el-select v-if="formula.type === '原材料'" v-model="formula.id" filterable placeholder="请选择">
              <el-option v-for="item in materials" :key="item.id" :label="item.name" :value="item.id" />
            </el-select>
            <el-select v-if="formula.type === '初级产品'" v-model="formula.id" filterable placeholder="请选择">
              <el-option v-for="item in basics" :key="item.id" :label="item.name" :value="item.id" />
            </el-select>
          </el-col>
          <el-col :span="2" class="count" style="text-align:center;">
            <span>数量</span>
          </el-col>
          <el-col :span="6">
            <el-input v-model="formula.count" autocomplete="off" />
          </el-col>
          <el-col :span="10" class="buttons" style="text-align:right;">
            <el-button type="danger" size="medium" icon="el-icon-remove-outline" @click="del(index)" />
            <el-button type="success" size="medium" icon="el-icon-circle-plus-outline" @click="showSelect()" />
          </el-col>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancleDialog">取消</el-button>
        <el-button v-show="showAdd()" type="success" @click="showSelect()">新增配方</el-button>
        <el-button type="primary" @click="dialogStatus==='create'?createData():updateData()">确认</el-button>
      </div>
    </el-dialog>

    <el-dialog :visible.sync="dialogVisible" width="30%" title="提示">
      <span>请选择添加的类型</span>
      <span slot="footer" class="dialog-footer">
        <el-button type="success" @click="addFormula(0)">添加原材料</el-button>
        <el-button type="primary" @click="addFormula(1)">添加初级产品</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { fetchList, createMiddle, updateMiddle, deleteMiddle } from '@/api/middle'
import { findAllBasic } from '@/api/basic'
import { findAllMaterial } from '@/api/material'
import { isNameValid, isNumberValid, notEmpty } from '@/utils/validates'
import { parseNumber } from '@/utils/utils'
import waves from '@/directive/waves' // waves directive
import { parseTime } from '@/utils'
import Pagination from '@/components/Pagination' // secondary package based on el-pagination
import Logger from 'chivy'

const log = new Logger('views/table/middle-table')

const calendarTypeOptions = [
  { key: 'CN', display_name: 'China' },
  { key: 'US', display_name: 'USA' },
  { key: 'JP', display_name: 'Japan' },
  { key: 'EU', display_name: 'Eurozone' }
]

// arr to obj, such as { CN : "China", US : "USA" }
const calendarTypeKeyValue = calendarTypeOptions.reduce((acc, cur) => {
  acc[cur.key] = cur.display_name
  return acc
}, {})

export default {
  name: 'CategoryTable',
  components: { Pagination },
  beforeRouteEnter(to, from, next) {
    next(vm => {
      vm.getList()
    })
  },
  directives: { waves },
  filters: {
    statusFilter(status) {
      const statusMap = {
        published: 'success',
        draft: 'info',
        deleted: 'danger'
      }
      return statusMap[status]
    },
    typeFilter(type) {
      return calendarTypeKeyValue[type]
    }
  },
  data() {
    return {
      materials: [],
      basics: [],
      tableKey: 0,
      list: null,
      total: 0,
      dialogVisible: false,
      listLoading: true,
      listQuery: {
        page: 1,
        limit: 10,
        name: undefined,
        sort: '+id'
      },
      importanceOptions: [1, 2, 3],
      calendarTypeOptions,
      sortOptions: [{ label: 'ID Ascending', key: '+id' }, { label: 'ID Descending', key: '-id' }],
      statusOptions: ['published', 'draft', 'deleted'],
      showReviewer: false,
      temp: {
        id: undefined,
        name: undefined,
        unit: '个',
        capacity: undefined,
        price: 0,
        formulaVos: [],
        pricePerUnit: 0
      },
      dialogFormVisible: false,
      dialogStatus: '',
      textMap: {
        update: '编辑',
        create: '添加'
      },
      dialogPvVisible: false,
      pvData: [],
      rules: {
        name: [{ required: true, trigger: 'blur', validator: isNameValid }],
        capacity: [{ required: true, trigger: 'blur', validator: isNumberValid }],
        unit: [{ required: true, trigger: 'blur', validator: notEmpty }],
        price: [{ required: true, trigger: 'blur', validator: isNumberValid }]
      },
      downloadLoading: false
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.listLoading = true
      fetchList(this.listQuery).then(response => {
        this.list = response.data
        this.total = response.totalRows
        this.listLoading = false
      })
    },
    getMaterials() {
      findAllMaterial().then(response => {
        this.materials = response.data
      })
    },
    getBasics() {
      findAllBasic().then(response => {
        this.basics = response.data
      })
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },
    handleModifyStatus(row, status) {
      this.$message({
        message: '操作成功',
        type: 'success'
      })
      row.status = status
    },
    sortChange(data) {
      const { prop, order } = data
      if (prop === 'id') {
        this.sortByID(order)
      }
    },
    sortByID(order) {
      if (order === 'ascending') {
        this.listQuery.sort = '+id'
      } else {
        this.listQuery.sort = '-id'
      }
      this.handleFilter()
    },
    resetTemp() {
      this.temp = {
        id: undefined,
        name: undefined,
        unit: '个',
        capacity: undefined,
        price: 0,
        formulaVos: [],
        pricePerUnit: 0
      }
    },
    handleCreate() {
      this.getMaterials()
      this.getBasics()
      this.resetTemp()
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    createData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          let showStop = true;
          this.temp.formulaVos.forEach(formula => {
            if (formula.count === '' || formula.id === '') {
              showStop = false
            }
          })
          if (showStop) {
            createMiddle(this.temp).then(() => {
              this.dialogFormVisible = false
              this.$notify({
                title: '成功',
                message: '创建中级产品[' + this.temp.name + ']',
                type: 'success',
                duration: 2000
              })
              this.getList()
            }).catch(() => {
              this.dialogFormVisible = false
              this.getList()
            })
          } else {
            this.$notify({
              title: '失败',
              message: '请完整填写配方资料',
              type: 'error',
              duration: 2000
            })
          }
        }
      })
    },
    handleUpdate(row) {
      this.getMaterials()
      this.getBasics()
      this.temp = Object.assign({}, row) // copy obj
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    updateData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          // 转换字符串为数字
          this.temp.formulaVos.forEach(formula => {
            formula.count = Number(formula.count)
          })
          const tempData = Object.assign({}, this.temp)
          // console.log("this.temp is ==> " + JSON.stringify(this.temp))
          updateMiddle(tempData).then(() => {
            this.dialogFormVisible = false
            this.$notify({
              title: '成功',
              message: '更新中级产品【' + tempData.name + '】成功',
              type: 'success',
              duration: 2000
            })
            this.getList()
          }).catch(() => {
            this.dialogFormVisible = false
            this.getList()
          })
        }
      })
    },
    handleDelete(row, index) {
      this.$confirm('此操作将永久删除中级产品【' + row.name + '】, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const data = {
          'id': row.id,
          'name': row.name
        }
        deleteMiddle(data).then(() => {
          this.$notify({
            title: '成功',
            message: '删除中级产品【' + row.name + '】成功',
            type: 'success',
            duration: 2000
          })
          this.getList()
        }).catch(() => {
          this.$notify({
            title: '失败',
            message: '删除中级产品【' + row.name + '】失败',
            type: 'error',
            duration: 2000
          })
        })
      }).catch(() => {
      })
    },
    formatJson(filterVal) {
      return this.list.map(v => filterVal.map(j => {
        if (j === 'timestamp') {
          return parseTime(v[j])
        } else {
          return v[j]
        }
      }))
    },
    getSortClass: function(key) {
      const sort = this.listQuery.sort
      return sort === `+${key}` ? 'ascending' : 'descending'
    },
    showExpire(status) {
      return status ? '需要' : '不需要'
    },
    clearName() {
      this.listQuery.name = undefined
      this.getList()
    },
    parseNumber(number) {
      return parseNumber(number)
    },
    del(index) {
      log.debug('del index = ' + JSON.stringify(this.temp.formulaVos[index]))
      this.temp.formulaVos.splice(index, 1)
    },
    addFormula(index) {
      const formula = {
        'count': '',
        'id': '',
        'name': '',
        'price': ''
      }
      if (index === 1) {
        formula.type = '初级产品'
      } else {
        formula.type = '原材料'
      }
      this.temp.formulaVos.push(formula)
      this.dialogVisible = false
    },
    showAdd() {
      log.debug('length = ' + JSON.stringify(this.temp))
      return this.temp.formulaVos.length === 0
    },
    cancleDialog() {
      this.dialogFormVisible = false
      this.getList()
    },
    showSelect() {
      this.dialogVisible = true
    }
  }
}
</script>
