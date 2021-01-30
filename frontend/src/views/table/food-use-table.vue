<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input v-model="listQuery.name" clearable placeholder="请输入耗材名字" style="width: 200px;" class="filter-item" @keyup.enter.native="handleFilter" @clear="clearName" />
      <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">查找</el-button>
      <el-select v-model="temp.categoryId" placeholder="请选择" clearable filterable @change="changeCategory" @clear="clearCategory">
        <!-- label是文字，value是值 -->
        <el-option v-for="item in categories" :key="item.id" :label="item.name" :value="item.id" />
      </el-select>
      <!-- <el-button class="filter-item" style="margin-left: 10px;" type="primary" icon="el-icon-edit" @click="handleCreate">添加</el-button> -->
    </div>

    <el-table :key="tableKey" v-loading="listLoading" :data="list" border fit highlight-current-row style="width: 100%;" @sort-change="sortChange">
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
      <el-table-column label="剩余量" min-width="150px" align="center">
        <template slot-scope="{row}">
          <span class="link-type" @click="handleUpdate(row)">{{ row.restCount }}</span>
        </template>
      </el-table-column>
      <el-table-column label="总量" min-width="150px" align="center">
        <template slot-scope="{row}">
          <span class="link-type" @click="handleUpdate(row)">{{ row.count }}</span>
        </template>
      </el-table-column>
      <el-table-column label="详情" min-width="150px" align="center">
        <template slot-scope="{row}">
          <span class="link-type" @click="handleUpdate(row)">{{ row.description }}</span>
        </template>
      </el-table-column>
      <el-table-column label="图片" min-width="120px" align="center">
        <template slot-scope="{row}">
          <img v-if="row.imageUrl" :src="row.imageUrl" alt="" class="link-type" width="100%" height="100%" @click="handleShowImage(row)"><img>
          <span v-if="!row.imageUrl" class="link-type" @click="handleUpdate(row)">无</span>
        </template>
      </el-table-column>
      <el-table-column label="所属分类" min-width="150px" align="center">
        <template slot-scope="{row}">
          <span class="link-type" @click="handleUpdate(row)">{{ row.categoryName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="购买时间" width="150px" align="center">
        <template slot-scope="{row}">
          <span class="link-type" @click="handleUpdate(row)">{{ row.inDate | parseTime('{y}-{m}-{d} {h}:{i}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="过期时间" width="150px" align="center">
        <template slot-scope="{row}">
          <span class="link-type" @click="handleUpdate(row)">{{ row.expireDate | parseTime('{y}-{m}-{d} {h}:{i}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="230" class-name="small-padding fixed-width">
        <template slot-scope="{row}">
          <el-button type="primary" size="mini" @click="handleUpdate(row)">使用</el-button>
          <!-- <el-button v-if="row.status!='published'" size="mini" type="success" @click="handleUse(row)">使用</el-button> -->
          <!-- <el-button v-if="row.status!='deleted'" size="mini" type="danger" @click="handleDelete(row,$index)">删除</el-button> -->
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getList" />

    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible" @close="closeDialog">
      <el-form ref="dataForm" :rules="rules" :model="temp" label-position="left" label-width="100px" style="width: 400px; margin-left:50px;">
        <el-form-item label="名字" prop="name">
          <el-input v-model="temp.name" :disabled="true" />
        </el-form-item>
        <el-form-item label="剩余量" prop="restCount">
          <el-input v-model="temp.restCount" :disabled="true" />
        </el-form-item>
        <el-form-item label="本次使用" prop="useCount">
          <el-input v-model="temp.useCount" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button type="primary" @click="dialogStatus==='create'?createData():updateData()">确认</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { fetchList, createFood, updateFood, deleteFood } from '@/api/food'
import { findAllCategory } from '@/api/category'
import { isNameValid, isNumberValid, isValueSelected } from '@/utils/validates'
import waves from '@/directive/waves' // waves directive
import { parseTime } from '@/utils'
import Pagination from '@/components/Pagination' // secondary package based on el-pagination
import Logger from 'chivy'

const log = new Logger('views/table/food-table')

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
  name: 'FoodUseTable',
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
      tableKey: 0,
      list: null,
      categories: [],
      showExprie: false,
      total: 0,
      listLoading: true,
      listQuery: {
        page: 1,
        limit: 10,
        name: undefined,
        categoryId: undefined,
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
        count: 0,
        restCount: 0,
        useCount: 0,
        inDate: new Date(),
        expireDate: undefined,
        imageUrl: undefined,
        description: undefined,
        categoryName: undefined,
        images: []
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
        name: [{ required: true, validator: isNameValid, trigger: 'blur' }],
        count: [{ required: true, validator: isNumberValid, trigger: 'blur' }],
        restCount: [{ required: true, validator: isNumberValid, trigger: 'blur' }],
        inDate: [{ required: true, type: 'date', message: '请选择日期', trigger: 'change' }],
        expireDate: [{ required: false, type: 'date', message: '请选择日期', trigger: 'change' }],
        categories: [{ required: true, validator: isValueSelected, trigger: 'change' }],
        useCount: [{ required: true, validator: isNumberValid }]
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
      this.getCategories()
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
        count: 0,
        restCount: 0,
        useCount: 0,
        inDate: new Date(),
        expireDate: undefined,
        imageUrl: undefined,
        description: undefined,
        categoryName: undefined
      }
    },
    getCategories() {
      findAllCategory().then(resp => {
        this.categories = resp.data
        log.debug('categories = ' + JSON.stringify(this.categories))
      })
    },
    handleCreate() {
      this.resetTemp()
      this.getCategories()
      this.showExprie = false
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    createData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          this.temp.restCount = this.temp.count
          this.temp.inDate = +new Date(this.temp.inDate)
          this.temp.expireDate = +new Date(this.temp.expireDate)
          createFood(this.temp).then(() => {
            this.dialogFormVisible = false
            this.$notify({
              title: '成功',
              message: '创建耗材[' + this.temp.name + ']',
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
    handleUpdate(row) {
      this.showExprie = false
      this.getCategories()
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
          const tempData = Object.assign({}, this.temp)
          log.debug('tempData = ' + JSON.stringify(tempData))
          // 使用数量大于剩余数量
          if (this.temp.useCount > this.temp.restCount) {
            this.dialogFormVisible = false
            this.$notify({
              title: '失败',
              message: '[' + tempData.name + ']仅剩余[' + tempData.restCount + ']份, 但要使用 [' + tempData.useCount + ']份',
              type: 'error',
              duration: 2000
            })
            this.getList()
          } else {
            tempData.inDate = +new Date(tempData.inDate)
            tempData.expireDate = +new Date(tempData.expireDate)
            // 剩余量
            tempData.restCount = tempData.restCount - tempData.useCount
            updateFood(tempData).then(() => {
              this.dialogFormVisible = false
              this.$notify({
                title: '成功',
                message: '更新耗材【' + tempData.name + '】成功',
                type: 'success',
                duration: 2000
              })
              this.getList()
            }).catch(() => {
              this.dialogFormVisible = false
              this.getList()
            })
          }
        }
      })
    },
    handleDelete(row, index) {
      this.$confirm('此操作将永久删除耗材【' + row.name + '】, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const data = {
          'id': row.id,
          'name': row.name
        }
        deleteFood(data).then(() => {
          this.$notify({
            title: '成功',
            message: '删除耗材【' + row.name + '】成功',
            type: 'success',
            duration: 2000
          })
          this.getList()
        }).catch(() => {
          this.$notify({
            title: '失败',
            message: '删除耗材【' + row.name + '】失败',
            type: 'error',
            duration: 2000
          })
        })
      }).catch(() => {
      })
    },
    handleFetchPv(pv) {
      // fetchPv(pv).then(response => {
      //   this.pvData = response.data.pvData
      //   this.dialogPvVisible = true
      // })
    },
    handleDownload() {
      this.downloadLoading = true
      import('@/vendor/Export2Excel').then(excel => {
        const tHeader = ['timestamp', 'title', 'type', 'importance', 'status']
        const filterVal = ['timestamp', 'title', 'type', 'importance', 'status']
        const data = this.formatJson(filterVal)
        excel.export_json_to_excel({
          header: tHeader,
          data,
          filename: 'table-list'
        })
        this.downloadLoading = false
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
    handleShowImage(row) {
      log.debug('row.image = ' + JSON.stringify(row.imageUrl))
      this.$alert('<img src=' + row.imageUrl + ' width="100%" height="100%">', '', {
        dangerouslyUseHTMLString: true,
        showConfirmButton: false
      })
    },
    handleOnSuccess(response, file, fileList) {
      this.temp.imageUrl = fileList
      log.debug('file = ' + JSON.stringify(file))
      log.debug('fileList = ' + JSON.stringify(fileList))
      log.debug('response = ' + JSON.stringify(response))
      log.debug('images = ' + JSON.stringify(this.temp.images))
      this.temp.imageUrl = response.data
    },
    beforeUpload(file) {
      // 文件大于10M
      if (file.size > 10 * 1024 * 1024) {
        this.$notify({
          title: '文件过大',
          message: '文件过大，只允许10M以下的文件',
          type: 'error',
          duration: 2000
        })
        return false
      }
    },
    changeCategory(value) {
      log.debug('category value is ' + JSON.stringify(value))
      this.listQuery.categoryId = value
      log.debug('listQuery value is ' + JSON.stringify(this.listQuery))
      this.getList()
    },
    closeDialog() {
    },
    clearCategory() {
      this.listQuery.categoryId = undefined
      this.getList()
    },
    clearName() {
      this.listQuery.name = undefined
      this.getList()
    }
  }
}
</script>
