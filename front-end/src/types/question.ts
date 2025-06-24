// 题目数据结构的接口
export interface Question {
  uid?: number; // 题目ID，可选（新增时不传）
  type?: "single_choice" | "multiple_choice" | "true_false" | "short_answer"; // 题目类型
  description?: string; // 题干内容
  options?: Record<string, string> | string; // 选项，单选/多选时使用，如 { A: '选项1', B: '选项2' }
  answer?: string; // 答案，单选/判断为字母/布尔，多选为 'A,B'，简答为文字
  explanation?: string; // 解析
  difficulty?: number; // 难度等级（1简单、2中等、3困难）
  tags?: string; // 标签，用英文逗号分隔
  createdAt?: string; // 创建时间（由后端返回）
  updatedAt?: string; // 更新时间（由后端返回）
}
