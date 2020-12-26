import { findAllCategory } from '@/api/category'
import Logger from 'chivy'
const log = new Logger('utils/validates')

const regexMatch = (str, regex) => new RegExp(regex).test(str)
// 不能有特殊字符
const noSpecialCharacter = '^[a-zA-Z0-9_\u4e00-\u9fa5\\s（）()%]+$'
// 正小数
const twoPointNumber = '^[0-9]+(.[0-9]{2})$'
const onePointNumber = '^[0-9]+(.[0-9]{1})$'
const noPointNumber = '^[1-9][0-9]*$'

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
    callback(new Error('请填写内容'))
  } else {
    callback()
  }
}

export const isNameValid = (rule, value, callback) => {
  log.debug('isNameValid value = ' + JSON.stringify(value))
  if (value === undefined) {
    callback(new Error('请输入名称'))
  } else {
    // 去掉空格
    value = value.replace(/\s+/g, '')
    log.debug('value = [' + value + ']')
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
}

export const isNumberValid = (rule, value, callback) => {
  value = value + ''
  log.debug('isNumberValid value = ' + JSON.stringify(value))
  // 去掉空格
  value = value.replace(/\s+/g, '')
  if (value === '') {
    callback(new Error('请输入数字'))
  } else {
    log.debug('isNumberValid debug value = ' + JSON.stringify(value))
    // debugger
    if (regexMatch(value, noPointNumber)) {
      log.debug('noPointNumber')
      callback()
    } else if (regexMatch(value, onePointNumber)) {
      log.debug('onePointNumber')
      callback()
    } else if (regexMatch(value, twoPointNumber)) {
      log.debug('twoPointNumber')
      callback()
    } else {
      callback(new Error('只支持正实数,最多小数点后2位'))
    }
  }
}

export const isValueSelected = (rule, value, callback) => {
  value = value + ''
  log.debug('isValueSelected value = ' + JSON.stringify(value))
  callback()
}

