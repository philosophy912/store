import { findAllCategory } from '@/api/category'
import Logger from 'chivy'
const log = new Logger('utils/validates')

const regexMatch = (str, regex) => new RegExp(regex).test(str)
// 不能有特殊字符
const noSpecialCharacter = '^[a-zA-Z0-9_\u4e00-\u9fa5\\s·]+$'

export const isCategoryNameUsed = (rule, value, callback) => {
  log.debug('isCategoryNameUsed value = ' + JSON.stringify(value))
  // 去掉空格
  value = value.replace(/\s+/g, '')
  if (value === '') {
    callback(new Error('耗材名称不能为空'))
  } else {
    if (!regexMatch(value, noSpecialCharacter)) {
      callback(new Error('名称不能有特殊字符'))
    } else {
      findAllCategory().then(response => {
        // log.debug('response is ' + JSON.stringify(response))
        const categories = response.data
        categories.forEach(category => {
          // log.debug('category is ' + JSON.stringify(category))
          if (value === category.name) {
            callback('名称重复，请检查')
          }
        })
        callback()
      })
    }
  }
}

export const notEmpty = (rule, value, callback) => {
  log.debug('notEmpty value = ' + JSON.stringify(value))
  // 去掉空格
  value = value.replace(/\s+/g, '')
  if (value === '') {
    callback(new Error('请选择至少一项'))
  } else {
    callback()
  }
}

export const isNameValid = (rule, value, callback) => {
  log.debug('isGoodsNameValid value = ' + JSON.stringify(value))
  // 去掉空格
  value = value.replace(/\s+/g, '')
  if (value === '') {
    callback(new Error('请输入名称'))
  } else {
    if (!regexMatch(value, noSpecialCharacter)) {
      callback(new Error('名称不能有特殊字符'))
    } else {
      callback()
    }
  }
}
